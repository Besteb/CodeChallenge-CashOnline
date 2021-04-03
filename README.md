# Cash Online Code Challenge

*Este repositorio es únicamente para fines relacionados con la revisión de código para una entrevista para Cash Online. **/** This repository is for code review purposes only for an interview for Cash Online.*
## Idioma/Language

* [Español](#español)
* [English](#english)

## Español
### Información
Este proyecto se ha realizado utilizando Spring Boot 2.4.4 y el SDK de Java 11.0.  
Logré un coverage de tests de líneas/funciones del 100% según los informes de IntelliJ IDEA.  
También agregué un par de endpoints porque sentí que mejoran la experiencia del usuario con la aplicación. Puede consultar la colección Postman actualizada [aquí] (https://www.getpostman.com/collections/19ddc3273cd99123f5dc)  

### Instalación

En primer lugar, asegúrese de haber instalado Java SDK 11.0 (debería poder descargarlo desde [aquí] (https://www.oracle.com/ar/java/technologies/javase-jdk11-downloads.html))

Utilice el administrador de paquetes [maven] (https://maven.apache.org/) para instalar Spring Boot y todas las dependencias.

### Buildear y correr el proyecto

En el root del repositorio utilice este comando para instalar dependencias y compilar
```bash
mvn clean install
```
Una vez finalizado el comando anterior utilice este para ejecutar la applicación
``` bash
mvn spring-boot:run
```

### Usar el servicio REST
La aplicación se ejecuta de forma predeterminada en el puerto 8080.  
Una vez que se esté ejecutando, puede hittear los endpoints utilizando [Postman] (https://www.postman.com/downloads/), curl u otra aplicación similar que pueda resultarle útil.


### Feedback

Cualquier feedback que puedan proporcionar es esperado y bien recibido.  
Si tienen alguna consulta por favor no duden en contactarme.

## English
### Info
This project has been done using Spring Boot 2.4.4 and Java 11.  
I achieved 100% function/lines test coverage based on the IntelliJ IDEA reports.  
I also added a couple of endpoints because I felt this improves the user experience with the app.  
You can check the updated Postman collection [here](https://www.getpostman.com/collections/19ddc3273cd99123f5dc)

### Installation

First make sure that you have installed Java SDK 11 (you should be able to download it from [here](https://www.oracle.com/ar/java/technologies/javase-jdk11-downloads.html))

Use the package manager [maven](https://maven.apache.org/) to install Spring Boot and all dependencies.

### Build and Run the Project

In the repo root run this command to install dependencies and build
```bash
mvn clean install
```
Then to run the application using this command
```bash
mvn spring-boot:run
```

### Use the REST service
The application is running by default on the port 8080.
Once it's running you can hit the endpoints using [Postman](https://www.postman.com/downloads/), curl, or another similar app that you may see useful.

### Feedback

Any feedback you can provide is expected and well-received. If you have any questions please do not hesitate to contact me.