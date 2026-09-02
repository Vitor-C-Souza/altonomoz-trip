# Altonomoz Trip

API para gestão de viagens/OSs com importação de planilhas, cálculo de valores e acompanhamento de recebimentos
pendentes.

## Visão geral

O projeto foi desenvolvido em Java com Spring Boot e tem como objetivo:

- registrar viagens com OS, origem, destino, paradas, distância, tempo e valor;
- importar dados de uma planilha pública/Google Sheets;
- calcular valores por quilômetro e por minuto;
- identificar recebimentos pendentes por data de vencimento;
- persistir os dados em MongoDB.

A estrutura segue uma arquitetura em camadas, com domínio, casos de uso, adaptadores de entrada/saída e persistência.

## Stack tecnológico

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data MongoDB
- Bean Validation
- Apache Commons CSV
- MongoDB Atlas / MongoDB

## Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:

- JDK 21+
- Maven 3.9+
- acesso a um banco MongoDB (Atlas ou local)
- URL da planilha/CSV do Google Sheets

## Variáveis de ambiente

O projeto lê as seguintes variáveis:

- `MONGO_USER`
- `MONGO_PASSWORD`
- `MONGO_CLUSTER`
- `CSV_URL`

Exemplo em PowerShell:

```powershell
$env:MONGO_USER="seu-usuario"
$env:MONGO_PASSWORD="sua-senha"
$env:MONGO_CLUSTER="cluster-exemplo"
$env:CSV_URL="https://docs.google.com/spreadsheets/.../export?format=csv"
```

## Como executar

1. Clone o repositório:

```bash
git clone https://github.com/Vitor-C-Souza/altonomoz-trip.git
cd altonomoz-trip
```

2. Configure as variáveis de ambiente.

3. Execute a aplicação:

```bash
./mvnw spring-boot:run
```

Ou no Windows:

```powershell
mvnw.cmd spring-boot:run
```

4. A API ficará disponível em:

```text
http://localhost:8080
```

## Endpoints principais

### Criar viagem

```http
POST /api/v1/trips
Content-Type: application/json
```

Exemplo de payload:

```json
{
  "os": "OS-001",
  "data": "2026-08-24T08:00:00",
  "origem": "São Paulo",
  "paradas": [
    "Campinas",
    "Sorocaba"
  ],
  "destino": "Rio de Janeiro",
  "km": 480.0,
  "tempo": "PT6H30M",
  "valor": 2800.00
}
```

Resposta:

```json
{
  "os": "OS-001",
  "data": "2026-08-24T08:00:00",
  "origem": "São Paulo",
  "destino": "Rio de Janeiro",
  "km": 480.0,
  "tempo": "PT6H30M",
  "valor": 2800.00,
  "valorPorKm": 5.83
}
```

### Importar viagens da planilha

```http
POST /api/v1/trips/import
```

Esse endpoint lê o CSV configurado em `CSV_URL` e salva os registros no MongoDB.

### Consultar recebimentos pendentes

```http
GET /api/v1/trips/recebimentos/pendentes
```

Resposta esperada:

```json
[
  {
    "dia": "2026-08-20",
    "valor": 18350.00
  },
  {
    "dia": "2026-08-31",
    "valor": 9200.00
  }
]
```

### Consultar ganhos por mês

```http
GET /api/v1/trips/ganhos/mensais
```

Resposta esperada:

```json
[
  {
    "mes": "2026-08",
    "valor": 27550.00
  },
  {
    "mes": "2026-09",
    "valor": 9100.00
  }
]
```

## Estrutura do projeto

```text
src/
├── main/
│   ├── java/
│   │   └── app/vitorcsouza/altonomoz_trip/
│   │       ├── application/
│   │       ├── domain/
│   │       ├── infrastructure/
│   │       └── AltonomozTripApplication.java
│   └── resources/
│       └── application.properties
├── test/
│   └── java/
└── pom.xml
```

## Observações

- O cálculo de data de recebimento usa a regra de fechamento por dezena do mês.
- A aplicação usa MongoDB para guarda dos dados de viagem.
- O fluxo de importação de planilha é pensado para extrair registros e persistir automaticamente no banco.

## Licença

Este projeto não especifica uma licença no momento. Verifique com o responsável do repositório antes de redistribuir o
código.
