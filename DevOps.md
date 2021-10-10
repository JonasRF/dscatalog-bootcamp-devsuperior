# dscatalog-bootcamp-devsuperior


## Instalação do Docker
 
https://docs.docker.com/get-docker

Instalando Docker Desktop no Windows: https://youtu.be/i_ZyYIdR02Q

Versão do Docker

```
docker -v
```

Versão Docker Client e Docker Server (Daemon)

```
docker version
```
Informações detalhadas do estado atual do Docker

```
docker info
```
## Imagens, containers e registry
 
### Imagem
 
•	Conceito: é a definição estática de como um container deve ser instanciado

•	Uma imagem é composta por camadas

•	Uma imagem tipicamente é definida a partir de outra imagem existente

•	Uma imagem "inicial" é definida a partir de "scratch"

### Container

•	Conceito: é uma instância de uma imagem

•	Pode estar em execução ou parado

•	Container é STATEFUL: mantém estado. Ele possui uma camada superior de escrita.

### Registry
 
•	É um serviço que armazena imagens

•	Registry padrão para Docker: Docker Hub

•	Para instanciar um container, a imagem precisa existir localmente ou em um Registry

## Comandos ps, images, pull e run, stop, start

Listar os containers (inclusive os parados: -a)

```
docker ps
```
Baixar uma imagem do Docker Hub (se não especificar tag, vem a "latest")

```
docker pull <image:tag>
```

Listar as imagens baixadas

```
docker images
```
Instanciar (e iniciar) um container com base em uma imagem (se não estiver baixada, baixa do Docker Hub)

```
docker run [OPTIONS] <image:tag> [COMMAND] [ARGS]
```
Documentação: https://docs.docker.com/engine/reference/run/

Exemplo 1: instancia um container do Ubuntu com linha de comando disponível

```
docker run -it ubuntu:20.04 /bin/bash
```
Exemplo 2: banco Postgres rodando na porta 5432 com base de dados "minha_base" e senha "123456"

```
docker run -p 5433:5432 --name meu-container-pg12 -e POSTGRES_PASSWORD=1234567 -e POSTGRES_DB=minha_base postgres:12-alpine
```
