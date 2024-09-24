package com.api.crud.repositories;

import com.api.crud.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//espeficicar a classe de entidade que esta utiliznado e o tipo do id
public interface ProductRepository extends JpaRepository<Product,Integer> {
    //repository vai conter os metodos para poder entrar em contato com o banco de dados
    //nesse caso o crud

    List<Product> findByName(String name);

}
