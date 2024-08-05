-- Deleta a tabela cliente se já existir
DROP TABLE IF EXISTS cliente;

-- Cria a tabela endereco se não existir (para referência)
DROP TABLE IF EXISTS endereco;

-- Cria a tabela cliente com as colunas e restrições especificadas
CREATE TABLE cliente (
    id SERIAL PRIMARY KEY, -- Equivalente a @Id e @GeneratedValue(strategy = GenerationType.IDENTITY)
    nome VARCHAR(50) NOT NULL CHECK (LENGTH(nome) BETWEEN 10 AND 50), -- Equivalente a @Column(nullable = false) e @Size(min = 10, max = 50)
    email VARCHAR(255) NOT NULL, -- Equivalente a @Column(nullable = false)
    cpf VARCHAR(20) NOT NULL, -- Equivalente a @Column(nullable = false)
    nascimento DATE NOT NULL, -- Equivalente a @Column(nullable = false) e @NotNull
    endereco_id INT, -- Referência para a tabela endereco
    CONSTRAINT email_unique UNIQUE (email), -- Garante que o email seja único
    CONSTRAINT cpf_unique UNIQUE (cpf) -- Garante que o cpf seja único
);

CREATE TABLE endereco (
    id SERIAL PRIMARY KEY,
    rua VARCHAR(255),
    cidade VARCHAR(255),
    estado VARCHAR(255),
    cep VARCHAR(20)
);

-- Adiciona a chave estrangeira de endereco_id na tabela cliente
ALTER TABLE cliente 
ADD CONSTRAINT fk_endereco
FOREIGN KEY (endereco_id) REFERENCES endereco (id)
ON DELETE CASCADE;