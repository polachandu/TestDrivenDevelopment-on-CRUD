package com.example.demo;

import com.example.demo.entity.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

	@LocalServerPort
	private int port;

	private String baseUrl = "http://localhost";

	private static RestTemplate restTemplate;

	@Autowired
	private TestH2Repo testH2Repo;

	@BeforeAll
	public static void init(){
		restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setup(){
		baseUrl = baseUrl+":" +port +"/products";
	}

	@Test
	public void testAddProduct(){
		Product product = new Product("headset",3,245);
		Product response = restTemplate.postForObject(baseUrl, product, Product.class);
		assertEquals("headset",response.getName());
		assertEquals(1,testH2Repo.findAll().size());
	}


	@Test
	@Sql(statements = "INSERT INTO PRODUCT_TBL (id, name, quantity, price)VALUES (4,'AC',1,3400)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM PRODUCT_TBL WHERE name = 'AC'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testGetAllProducts(){
		List<Product> list = restTemplate.getForObject(baseUrl, List.class);
		assertEquals(1, testH2Repo.findAll().size());
	}

	@Test
	@Sql(statements = "INSERT INTO PRODUCT_TBL (id, name, quantity, price)VALUES (1,'REMOTE',4,40)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM PRODUCT_TBL WHERE id = 1", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testGetProductById(){
		Product product = restTemplate.getForObject(baseUrl+"/{id}", Product.class,1 );
		assertAll(
				()-> assertNotNull(product),
		()->assertEquals("REMOTE", product.getName()),
		()->assertEquals(1,product.getId())
		);

	}

	@Test
	@Sql(statements = "INSERT INTO PRODUCT_TBL (id, name, quantity, price)VALUES (2,'SHOES',4,240)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM PRODUCT_TBL WHERE id = 2", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testUpdateProduct(){
		Product product = new Product("SHOES", 4, 200);
		restTemplate.put(baseUrl+"/update/{id}", product, 2);
		Product productFromDB = testH2Repo.findById(2).get();
		assertEquals(200, productFromDB.getPrice());
	}


	@Test
	@Sql(statements = "INSERT INTO PRODUCT_TBL (id, name, quantity, price) VALUES(3,'BATTERY',6, 20)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void testDeleteProduct(){
//		Product product = new Product("BATTERY", 6 , 20);
		restTemplate.delete(baseUrl+"/delete/{id}",  3);
		assertEquals(0, testH2Repo.findAll().size());
	}


}
