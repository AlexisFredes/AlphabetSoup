# Titulo: Sopa de letras
# Autor: Alexis Fredes

_El presente repositorio es una api rest full la cual por medio de los endPoint creara
una sopa de letras totalmente aleatoria, tener en cuenta que las palabras se pueden
cruzar entre si_

# Tecnologias

* Maven
* Java 
* SpringBoot

# Requisitos previos

* Tener instalado Java
* Tener instalado Git
* Tener instalado Docker

# Clonando repositorio

_Dirigirnos desde consola a donde queremos clonar el repositorio ya se el escritorio
o una carpeta de destino y colocaremos el siguiente comando

```
git clone https://github.com/AlexisFredes/AlphabetSoup.git
```
_y listo._

## Compilando el proyecto

* realizar un pull del repository de hub docker.
```
docker pull alexisfredes99/stkapp
```
* correr la imagen docker con el siguiente comando:
```
docker run -d -p 8080:8080 alexisfredes99/stkapp
```

* para revisar que la imagen este corriendo:
```
docker ps
```
_si salio todo bien, nos deberia mostrar la imagen que acabamos de correr_

### EndPoints de ejemplos
#### Metodo post
```
 Url: http://localhost:8080/alphabetSoup
 ```
* body:
```
{
    "words": "palabra1 - palabras2",
    "w":15,
    "h":15,
    "ltr":true,
    "rtl":true,
    "ttb":true,
    "btt":true,
    "d":true
}
```
_a tener en cuenta:_
- words = aqui colocaremos las palabras que queramos que aparescan en la sopa
          separadas con un guion intermedio "-"
- w = Ancho de la matriz
- h = Alto de la matriz
### posicion de las palabras
- ltr = de izquierda a derecha
- rtl = de derecha a izquierda
- ttb =  de arriba hacia abajo
- btt = de abajo hacia arriba
- d =  en diagonal

_Este metodo post retornada un id que se usara para buscar la sopa de letras_

#### Metodo get
```
Url: http://localhost:8080/alphabetSoup/view/ID_QUE_RETORNA_EL_METODO_POST
```
_Este endpoint va a retorna la sopa de letras en texto plano para que la podamos
visualizar correctamente_

```
Url: http://localhost:8080/alphabetSoup/list/ID_QUE_RETORNA_EL_METODO_POST
```
_Este endpoint va a retorna la lista de palabras que se encuentran en la sopa de letras_

#### Metodo put
```
Url: http://localhost:8080/alphabetSoup/ID_QUE_RETORNA_EL_METODO_POST
```
* body:
```
{
    "sr": 1,
    "sc": 5,
    "er": 1,
    "ec": 12
}
```
_a tener en cuenta:_
- sr = fila donde comienza la palabra
- sc = columna donde comienza la palabra
- er = fila donde termina la palabra
- ec = columna donde termina la palabra


#### Metodo delete
```
Url: http://localhost:8080/alphabetSoup/ID_QUE_RETORNA_EL_METODO_POST
```
_Este metodo borrara la sopa de letras con el id correspondiente al del parametro_
