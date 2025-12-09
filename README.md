# API de Conta Bancária com Integração IoT

![Java](https://img.shields.io/badge/Java-21-orange.svg) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.5-green.svg) ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat&logo=springsecurity) ![JWT](https://img.shields.io/badge/JWT-black?style=flat&logo=jsonwebtokens) ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=swagger) ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white)

API RESTful desenvolvida no contexto académico do SENAI para simular um sistema bancário completo. O diferencial deste projeto é o módulo de **Autenticação IoT**, que adiciona uma camada extra de segurança às transações financeiras (saques, transferências e pagamentos) através da validação biométrica simulada via protocolo MQTT.

---

## 🚀 Visão Geral das Funcionalidades

### 🔐 Segurança & IoT (Destaque)
* **Autenticação JWT:** Sistema de login robusto com tokens de acesso (`Bearer Token`).
* **Vinculação de Dispositivos IoT:** Permite que clientes cadastrem dispositivos físicos (simulados) para validação de segurança via `DispositivoIoTController`.
* **Validação 2FA via MQTT:** Operações críticas iniciam um fluxo de autenticação assíncrona. O backend publica no tópico `banco/autenticacao/iniciar` e aguarda validação no tópico `banco/autenticacao/validar`.
* **Polling de Status:** Endpoint dedicado para o frontend verificar o estado da autenticação biométrica (`PENDENTE`, `AUTORIZADO`, `EXPIRADO`).

### 🏦 Operações Bancárias
* **Gestão de Contas:**
    * Criação e consulta de Contas Corrente (com limite e taxa) e Poupança (com rendimento).
    * Regras de negócio específicas para cada tipo de conta.
* **Transações Financeiras:**
    * **Pagamentos:** Processamento de pagamentos de boletos e impostos com cálculo automático de taxas configuráveis.
    * **Transferências:** Movimentação entre contas com validação de saldo.
    * **Saques e Depósitos:** Operações básicas de caixa.
* **Taxas Dinâmicas:** O sistema permite ao Gerente (Admin) cadastrar taxas (percentuais ou fixas) que são aplicadas automaticamente com base no tipo de pagamento (`BOLETO`, `CONTA_CONSUMO`, `IMPOSTO`).

### 👥 Gestão de Utilizadores
* **Perfis de Acesso (RBAC):**
    * `CLIENTE`: Acesso às próprias contas e operações financeiras.
    * `ADMIN` (Gerente): Gestão de taxas, dispositivos e contas.
* **Cadastro:** Fluxo de registo para novos clientes anexando contas iniciais.

---

## 🛠️ Tecnologias Utilizadas

* **Java 21**: Linguagem base da aplicação.
* **Spring Boot 3.5.5**: Framework principal.
* **Spring Security & JJWT**: Para autenticação e autorização stateless.
* **Spring Doc OpenAPI (Swagger)**: Documentação interativa da API.
* **MySQL**: Base de dados relacional para persistência dos dados.
* **Spring MQTTX**: Integração facilitada com brokers MQTT.
* **Lombok**: Para redução de código boilerplate.

---

## 🏃‍♀️ Como Correr a Aplicação

1.  **Pré-requisitos:**
    * Java JDK 21 instalado.
    * Maven instalado.
    * MySQL Server a correr (ou Docker container).
    * Um Broker MQTT (ex: Mosquitto) a correr na porta 1883 (ou configurar no `application.properties`).

2.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/samuelcorreia07/senai-conta-bancaria-spring-api.git](https://github.com/samuelcorreia07/senai-conta-bancaria-spring-api.git)
    cd senai-conta-bancaria-spring-api
    ```

3.  **Configuração da Base de Dados:**
    Verifique o ficheiro `src/main/resources/application.properties` e ajuste as credenciais do MySQL se necessário:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/senai_conta_bancaria_spring_api?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    spring.datasource.username=root
    spring.datasource.password=sua_senha
    ```

4.  **Execute o projeto:**
    ```bash
    mvn spring-boot:run
    ```

5.  **Aceda à API:**
    * A aplicação iniciará na porta padrão `8080`.
    * **Documentação Swagger:** Aceda a `http://localhost:8080/swagger-ui.html` para testar todos os endpoints.

---

## 🔑 Acesso Administrativo Inicial

O sistema possui uma configuração de *bootstrap* (`AdminBootstrap`) que cria um utilizador administrador provisório na primeira execução.

* **Email:** `admin@contabancaria.com`
* **Senha:** `admin123`

Utilize estas credenciais no endpoint `/auth/login` para obter o token JWT inicial.

---

## 📱 Fluxo de Autenticação IoT

Para realizar operações como **Saques**, **Transferências** ou **Pagamentos**, o fluxo de segurança é o seguinte:

1.  **Vínculo:** O Admin vincula um dispositivo ao cliente via `POST /api/dispositivos-iot/vincular`.
2.  **Início da Operação:** O cliente solicita (ex: `POST /api/pagamentos`).
    * O sistema valida os dados.
    * O sistema envia uma mensagem MQTT para o dispositivo do cliente.
    * A API retorna um `codigoId` e status `PENDENTE`.
3.  **Validação Física:** O dispositivo IoT lê a biometria e publica a confirmação no tópico MQTT `banco/autenticacao/validar`.
4.  **Polling:** O frontend consulta periodicamente `GET /api/autenticacao-iot/status/{codigoId}`.
5.  **Confirmação:** Assim que o status mudar para `AUTORIZADO`, o frontend chama o endpoint de confirmação (ex: `POST /api/pagamentos/confirmar`) para efetivar a transação.
