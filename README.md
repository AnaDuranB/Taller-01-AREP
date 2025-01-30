# AREP Taller 1 💻

## Diseño y Estructuración de Aplicaciones Distribuidas en Internet

Este proyecto implementa un servidor web en Java que maneja múltiples solicitudes de manera secuencial (no concurrente). El servidor es capaz de leer archivos del disco local y devolver cualquier archivo solicitado, incluyendo páginas HTML, archivos JavaScript, CSS e imágenes. Además, se desarrolla una aplicación web con comunicación asíncrona a través de servicios REST en el backend. 

En la aplicación web podrás añadir los componentes que quieres y te hacen falta para armar tu computador deseado. 😎

## Aplicación ejecutada
![image](https://github.com/user-attachments/assets/44185ea0-7236-45b4-936c-231ad36210ec)

![image](https://github.com/user-attachments/assets/eb18e0e2-d62c-4486-b4d6-26d3bbd63cbf)

![image](https://github.com/user-attachments/assets/d8805f6a-3eca-42fe-a60f-978070257f37)

![image](https://github.com/user-attachments/assets/c4445cc5-df94-48f6-88fc-b39446890ab2)

![image](https://github.com/user-attachments/assets/80b049e2-7faf-4578-8339-cabd6ee5eefb)

![image](https://github.com/user-attachments/assets/9692624e-5ec5-471f-90ba-79e016adcac3)


## Getting Started

Estas instrucciones te permitirán obtener una copia del proyecto en funcionamiento en tu máquina local para desarrollo y pruebas 🏋️.

### Prerequisites

Para ejecutar este proyecto necesitas instalar lo siguiente:

```
- Java 17 o superior
- Maven 3.8.1 o superior
- Un navegador web
```
En caso de no tener maven instalado, aquí encuentras un tutorial [Maven](https://dev.to/vanessa_corredor/instalar-manualmente-maven-en-windows-10-50pb).

### Installing

Sigue estos pasos para obtener un entorno de desarrollo funcional:

1. Clona este repositorio:

```
git clone https://github.com/AnaDuranB/Taller-01-AREP.git
```

2. Ingresa al directorio del proyecto:

```
cd Taller-01-AREP
```

En caso de no contar con un IDE de java que se haga responsable de la compilación y ejecución:

3. Compila el proyecto con Maven:

```
mvn clean compile
```

4. Ejecuta el servidor:

```
java -cp target/classes org.example.HttpServer
```
![image](https://github.com/user-attachments/assets/6904a1e6-3188-4918-a61d-832ee00906b8)

5. Abre tu navegador y accede a:

```
http://localhost:35000/
```

## Running the tests

### Pruebas end-to-end

Para ejecutar las pruebas automatizadas:

```
mvn test
```
![image](https://github.com/user-attachments/assets/2868f8c6-49b1-4de1-9273-25bd235d80bc)


Estas pruebas verifican la correcta respuesta del servidor ante diferentes solicitudes.

### Estilo de código

Se sigue el estándar de codificación recomendado para Java. Se pueden usar herramientas como Checkstyle para validar el formato.

## Deployment

Para desplegar este servidor en un sistema en producción:

1. Empaqueta el proyecto en un JAR ejecutable:

```
mvn clean package
```

2. Ejecuta el JAR generado:

```
java -cp target/PrepTaller1-1.0-SNAPSHOT.jar org.example.HttpServer
```

## Built With

- [Java SE](https://www.oracle.com/java/) - Lenguaje de programación
- [Maven](https://maven.apache.org/) - Herramienta de gestión de dependencias y construcción

## Authors

- Ana Maria Duran - *AREP* *Taller 1* - [AnaDuranB](https://github.com/AnaDuranB)

## Acknowledgments

- Inspiración y referencia de servidores web minimalistas en Java
- Documentación de Oracle sobre manejo de sockets en Java
