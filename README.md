# FluxoPago - 🚀 Sistema de Pedidos e Pagamentos

Este é um projeto de **Backend robusto** desenvolvido com **Spring Boot 3**, focado no gerenciamento de usuários, produtos e fluxo completo de pedidos (criação, adição de itens, pagamento com baixa de estoque e cancelamento).

O projeto utiliza **Docker** para orquestração de serviços, **Flyway** para versionamento de banco de dados e segue os princípios de **Clean Architecture** e **SOLID**.

## 🛠️ Tecnologias Utilizadas

* **Java 17** & **Spring Boot 3**
* **Spring Data JPA** (Persistência de dados)
* **PostgreSQL** (Banco de dados relacional)
* **Flyway** (Migrações de banco de dados)
* **Docker & Docker Compose** (Containerização)
* **MapStruct** (Mapeamento de DTOs)
* **Lombok** (Produtividade)
* **Swagger/OpenAPI** (Documentação interativa)

-----

## 🏗️ Arquitetura e Organização

O projeto está dividido em camadas para facilitar a manutenção e escalabilidade:

* **`business`**: Contém as regras de negócio, serviços, mappers e controladores.
* **`infrastructure`**: Responsável pelas entidades JPA, repositórios, exceções personalizadas e configurações de banco.
* **`config`**: Configurações globais, como a documentação do Swagger.

-----

## 🚀 Como Executar o Projeto

### Pré-requisitos

* **Docker** e **Docker Compose** instalados.
* Um arquivo `.env` na raiz do projeto com as seguintes variáveis:
  ```env
  DB_NAME=sistema_pedidos
  DB_USER=seu_usuario
  DB_PASSWORD=sua_senha
  DB_PORT=5432
  DB_HOST=postgres
  SPRING_PROFILES_ACTIVE=dev
  ```

### Passo a Passo

1.  **Clone o repositório:**

    ```bash
    git clone https://github.com/BryanGabriell/sistema-de-pedidos-e-pagamentos.git
    cd sistema-de-pedidos-e-pagamentos
    ```

2.  **Suba os containers (Banco e Aplicação):**

    ```bash
    docker-compose up --build
    ```

    *Isso irá compilar o Java dentro do container, rodar as migrações do Flyway e iniciar a API na porta 8080.*

-----

## 📖 Documentação da API (Swagger)

Com a aplicação rodando, você pode acessar a documentação interativa e testar os endpoints através do link:
👉 `http://localhost:8080/swagger-ui/index.html`

-----

## ⚙️ Fluxo de Funcionamento

1.  **Usuários e Produtos:** Cadastre usuários e produtos para alimentar o estoque.
2.  **Pedido:** Inicie um pedido para um usuário. O status inicial será `CRIADO`.
3.  **Itens:** Adicione itens ao pedido. O sistema calcula automaticamente o valor total.
4.  **Pagamento:** Ao pagar o pedido (`/pagar`), o sistema valida se há estoque disponível e realiza a baixa automática.
5.  **Cancelamento:** Pedidos podem ser cancelados, desde que não tenham sido pagos.

-----

## 🗄️ Modelo de Dados (Flyway)

O banco de dados é inicializado automaticamente via Flyway com as seguintes tabelas:

* `users`: Informações de clientes.
* `produtos`: Catálogo e controle de estoque.
* `pedidos`: Cabeçalho do pedido e status.
* `item_pedidos`: Detalhamento dos produtos em cada pedido.

-----

## ✒️ Autor

* **Bryan Gabriel** - [LinkedIn](https://www.linkedin.com/in/bryan-gabriell?utm_source=share_via&utm_content=profile&utm_medium=member_ios) - [GitHub](https://github.com/BryanGabriell)

-----

*Este projeto foi desenvolvido como parte do meu portfólio de Engenharia de Software.*
