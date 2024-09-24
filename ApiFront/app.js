document.addEventListener('DOMContentLoaded', function () {
    // vai carregar todos os produtos quando a página é carregada
    fetchProducts();

    // aq fica resposavel com o envio do formulário de produto (para criar/atualizar)
    document.getElementById('productForm').addEventListener('submit', function (e) {
        e.preventDefault();

        const name = document.getElementById('name').value;
        const price = document.getElementById('price').value;
        const id = document.getElementById('productId').value;

        const url = id ? `http://localhost:8080/products/update-product` : `http://localhost:8080/products/create-product`;
        const method = id ? 'PUT' : 'POST';

        // vai enviar a requisição ao backend
        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ id: id ? parseInt(id) : undefined, name: name, price: parseInt(price) })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('A resposta da rede não foi bem sucedida');
            }
            fetchProducts();  // Atualiza a lista de produtos após salvar
            document.getElementById('productForm').reset();  // Limpa o formulário após a operação
        })
        .catch(error => console.error('Ocorreu um problema com a operação fetch:', error));
    });

    // Lida com a busca de produto por ID
    document.getElementById('searchForm').addEventListener('submit', function (e) {
        e.preventDefault();
        const id = document.getElementById('searchId').value;

        fetch(`http://localhost:8080/products/by-id?id=${id}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('A resposta da rede não foi bem sucedida');
                }
                return response.json();
            })
            .then(product => {
                document.getElementById('productId').value = product.id;
                document.getElementById('name').value = product.name;
                document.getElementById('price').value = product.price;
            })
            .catch(error => console.error('Ocorreu um problema com a operação fetch:', error));
    });
});

// Função para buscar todos os produtos e exibir na tabela
function fetchProducts() {
    fetch('http://localhost:8080/products')
        .then(response => {
            if (!response.ok) {
                throw new Error('A resposta da rede não foi bem-sucedida');
            }
            return response.json();
        })
        .then(products => {
            const productList = document.getElementById('productList');
            productList.innerHTML = '';  // Limpa a lista atual
            products.forEach(product => {
                const row = document.createElement('tr');

                row.innerHTML = `
                    <td>${product.id}</td>
                    <td>${product.name}</td>
                    <td>${product.price}</td>
                    <td>
                        <button class="btn btn-danger btn-sm" onclick="deleteProduct(${product.id})">Delete</button>
                    </td>
                `;
                productList.appendChild(row);  // vai Adicionar o produto na tabela
            });
        })
        .catch(error => console.error('Ocorreu um problema com a operação fetch:', error));
}

// Função para deletar um produto
function deleteProduct(id) {
    fetch(`http://localhost:8080/products/delete-product?id=${id}`, {
        method: 'DELETE',
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('A resposta da rede não foi bem-sucedida');
        }
        fetchProducts();  // vai Atualizar a lista de produtos após deletar
    })
    .catch(error => console.error('Ocorreu um problema com a operação fetch:', error));
}
