package com.api.crud.controllers;

import com.api.crud.model.Product;
import com.api.crud.repositories.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
//estou informando para a IDE que no momento que ela compila o projeto toda a classe se trata
//de uma classe controller
@RequestMapping("/products")
//no momento que fizer as requisições http passando como parametro na url esse parametro
//estou informando que em /products estou utilizando todos os metodos contidos nessa classe


public class ProductController {//serve para receber as requisições http e devolver informações para o cliente(usuario)
    //conter os metodos para fazer essas tratativas


    @Autowired//faz todas as injeções de dependencias necessarias para usar o objeto
    ProductRepository repository;


    @GetMapping//estou informando para o metodo abaixo que ele vai ser do tipo get

    public ResponseEntity getAll() {//responseEntity é uma classe que é utilizada como padrao em apirest, pois
        //ja possuem metodos específicos
        List<Product> listProducts = repository.findAll(); //trazer todos os produtos do tipo produtos que estiverem cadastrados na base de dados
        return ResponseEntity.status(HttpStatus.OK).body(listProducts);
    }

    @Operation(description = "Busca o Produto pelo ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Retorna o Produto com seus dados"),
    @ApiResponse(responseCode = "400", description = "Não existe produto com esse Id")})
    @GetMapping("/by-id")
    public ResponseEntity getProductById(@RequestParam Integer id) {
        Optional<Product> product = repository.findById(id);
        if (product.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(product.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(description = "Cria um novo Produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao criar o produto")
    })
    @PostMapping("/create-product")
    public ResponseEntity createProduct(@   Valid @RequestBody Product product) {
        Product savedProduct = repository.save(product);
        if (savedProduct != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Operation(description = "Atualiza um Produto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Produto não encontrado para o ID fornecido")
    })
    @PutMapping("/update-product")
    public ResponseEntity updateProduct(@RequestBody Product product) {
        Optional<Product> productOptional = repository.findById(product.getId());//optional pois ele pode nao existir
        if (productOptional.isPresent()) {
            Product existingProduct = productOptional.get();
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            //atualiza os campos se necessário
            repository.save(existingProduct);
            return ResponseEntity.status(HttpStatus.OK).body(existingProduct);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(description = "Deleta um Produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Produto não encontrado para o ID fornecido")
    })
    @DeleteMapping("delete-product")
    public ResponseEntity deleteProduct(@RequestParam Integer id) {
        Optional<Product> productOptional = repository.findById(id);
        if (productOptional.isPresent()) {
            repository.delete(productOptional.get());
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @Operation(description = "Busca Produtos pelo nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Nenhum produto encontrado com o nome fornecido")
    })
    @GetMapping("/by-name")
    public ResponseEntity getProductByNome(@RequestParam String name) {
        List<Product> products = repository.findByName(name);

        if(products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }


}
