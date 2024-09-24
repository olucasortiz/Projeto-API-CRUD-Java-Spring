package com.api.crud.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
//@entity esta falando para nossa IDE que estamos usando uma anotacao
//especificar a utilidade de algo
//espficiando que essa classe se trata de uma entidade
@Entity(name = "product")
@Table(name = "product")

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Nome nao pode ser nulo")
    @Size(min = 2, max = 100, message = "Nome tem que ter entre 2 a 100 caracteres")
    private String name;

    @NotNull(message = "Preço não pode ser nulo")
    @Min(value = 0, message = "Preço tem que ser maior que 0")
    private Long price;


    public Product() {
    }

    public Product(Integer id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
