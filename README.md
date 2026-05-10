# OSO_Game

Aplicación cliente-servidor en Java para el juego Oso, con interfaz gráfica Swing y un canal de chat separado.

## 1. Objetivo de la aplicación

El sistema permite que dos jugadores participen en una partida en un tablero rectangular. Cada jugador elige, en cada turno, una posición y una letra entre `O` y `S`. La aplicación identifica formaciones válidas de "oso" y contabiliza los puntos de cada jugador. En paralelo, existe un chat independiente para comunicación entre los participantes.

## 2. Funcionalidades

- Creación de un servidor de juego con tablero configurable.
- Creación de un servidor de chat independiente.
- Interfaz gráfica para selección de letras y marcación de jugadas.
- Control de turnos con bloqueo y desbloqueo del tablero.
- Actualización del marcador de osos del jugador local y del adversario.
- Detección automática de osos formados en horizontal, vertical y diagonales.
- Finalización de la partida con indicación de ganador, perdedor o empate.
- Persistencia simple de victorias por jugador en archivo de texto.

## 3. Arquitectura

La aplicación está organizada en cuatro capas principales:

- `GameLogic`: reglas de negocio del juego, tablero y estado de la partida.
- `Network`: comunicación cliente-servidor para juego y chat.
- `UI`: componentes Swing que componen el tablero y los botones.
- `Persistence`: grabación y lectura del historial de victorias.

### 3.1 Componentes principales

- `Main.EjecutorServers`: punto de entrada del lado servidor. Solicita filas y columnas al operador e inicia los dos servidores.
- `Network.GameServer`: servidor de la partida. Recibe jugadas, actualiza el estado compartido y distribuye los mensajes a los clientes.
- `Network.ServidorChat`: servidor de chat. Reenvía mensajes recibidos a todos los clientes conectados.
- `Main.GUILog`: punto de entrada del cliente. Muestra la interfaz Swing e inicia las conexiones con los dos servidores.
- `Network.JuegoOso`: cliente de la partida. Envía jugadas y recibe actualizaciones del juego.
- `Network.ChatConfig`: cliente del chat. Envía y recibe mensajes.
- `GameLogic.Juego`: reglas de validación de las jugadas y conteo de osos.
- `GameLogic.GameState`: estado global de la partida, incluyendo turno y marcador.
- `GameLogic.Tablero`: representación matricial del tablero.
- `UI.TableroGUI` y `UI.OsoButton`: interfaz visual del tablero y de los botones.
- `Persistence.Log`: persistencia de las victorias en el archivo `src/DATA/log.txt`.

## 4. Flujo de ejecución

### 4.1 Servidor

1. El operador ejecuta `Main.EjecutorServers`.
2. El programa solicita el número de filas y columnas.
3. Se inicializan dos servidores:
	- juego en el puerto `12001`
	- chat en el puerto `12000`
4. El servidor de juego crea el estado inicial de la partida.
5. Al conectarse, cada cliente recibe las dimensiones del tablero.

### 4.2 Cliente

1. El usuario ejecuta `Main.GUILog`.
2. Se abre la ventana principal con los campos de configuración.
3. El usuario ingresa nombre, dirección IP y puertos de los dos servicios.
4. Al hacer clic en conectar, el cliente abre la sesión de chat y la sesión de juego.
5. El tablero es creado con el tamaño recibido del servidor.
6. El jugador elige la letra en una celda y envía la jugada.
7. El servidor distribuye la jugada, actualiza el estado y libera el turno del siguiente jugador.

## 5. Especificación funcional

### 5.1 Reglas del tablero

- El tablero es una matriz de celdas vacías al inicio de la partida.
- Cada celda acepta solo una letra por juego.
- El jugador puede alternar la selección de la celda entre `O`, `S` y vacía antes de enviar.
- Después de enviada, la celda correspondiente se deshabilita en el cliente.

### 5.2 Formación de oso

Un oso se identifica cuando existe una `S` en el centro y dos `O` adyacentes en una de estas direcciones:

- horizontal
- vertical
- diagonal principal
- diagonal secundaria

Cada conjunto válido se cuenta solo una vez.

### 5.3 Fin de partida

La partida termina cuando no quedan celdas vacías en el tablero. Al finalizar, el servidor compara la cantidad de osos formados por cada jugador y envía uno de los mensajes:

- `GANADOR`
- `PERDEDOR`
- `EMPATE`

## 6. Especificación protocolar

El protocolo es binario simple basado en `DataInputStream` y `DataOutputStream`, usando `writeUTF` y `readUTF`. No hay serialización de objetos ni JSON.

### 6.1 Conexión del juego

#### Cliente a servidor

- `NOMBRE,<nombre>`: identificación inicial del jugador.
- `<fila>,<columna>,<letra>`: jugada enviada al servidor.

#### Servidor a cliente

- `<filas>`: número de filas del tablero.
- `<columnas>`: número de columnas del tablero.
- `<fila>,<columna>,<letra>`: jugada confirmada y distribuida a todos los clientes.
- `BLOCK`: bloquea el tablero y el botón de envío.
- `UNBLOCK`: libera el turno del jugador actual.
- `OSOS,<misOsos>,<ososRivales>`: actualización del marcador.
- `OSOUBI,<fila>,<columna>`: indica una celda que participó en un oso.
- `GANADOR`: fin de la partida con victoria.
- `PERDEDOR`: fin de la partida con derrota.
- `EMPATE`: fin de la partida empatada.

### 6.2 Conexión del chat

#### Cliente a servidor

- `<usuario>-> <mensaje>\n`: mensaje textual enviado al chat.

#### Servidor a cliente

- cualquier mensaje recibido se retransmite a todos los clientes conectados.

## 7. Estructura de persistencia

El archivo `src/DATA/log.txt` almacena el contador de victorias por jugador en el formato:

```text
nombreDelJugador: cantidadDeVictorias
```

Cada vez que un jugador gana, el servidor incrementa el contador correspondiente y regrabará el archivo.

## 8. Cómo ejecutar

### 8.1 Requisitos previos

- Java Development Kit instalado.
- Entorno con interfaz gráfica disponible para ejecutar Swing.
- Permiso para abrir conexiones locales en los puertos `12000` y `12001`.

### 8.2 Compilación

Si el proyecto se compila manualmente, utilice la raíz del repositorio como directorio de trabajo e incluya `src` en el classpath.

Ejemplo en Windows PowerShell:

```powershell
javac -d bin -cp src;lib/* src\Main\EjecutorServers.java src\Main\GUILog.java
```

### 8.3 Ejecución del servidor

```powershell
java -cp bin;lib/* Main.EjecutorServers
```

Al iniciar, indique las dimensiones del tablero. El rango esperado es:

- filas entre 3 y 5
- columnas entre 3 y 6

### 8.4 Ejecución del cliente

```powershell
java -cp bin;lib/* Main.GUILog
```

En la pantalla de configuración, use:

- servidor del juego: IP y puerto `12001`
- servidor del chat: IP y puerto `12000`

## 9. Reproducibilidad

Es responsabilidad del estudiante garantizar la reproducibilidad de la aplicación. Para que la ejecución pueda ser reproducida por otra persona, debe informarse al menos:

- versión de Java utilizada
- sistema operativo y arquitectura
- comandos de compilación y ejecución
- dimensiones elegidas para el tablero en el servidor
- puertos utilizados por los servicios
- dependencias externas, si las hay
- archivos de datos necesarios, como `src/DATA/log.txt`

Si la entrega requiere reproducibilidad estricta, se recomienda registrar también:

- versión del IDE o herramienta de construcción
- ruta exacta de los artefactos generados
- pasos para ejecutar servidor y clientes en orden correcto
- cantidad mínima de instancias del cliente para iniciar una partida

## 10. Observaciones de uso

- El juego depende de dos clientes para la interacción completa de la partida.
- El chat y el juego son servicios separados y pueden inicializarse en el mismo host.
- El directorio `src/DATA` contiene imágenes utilizadas por la interfaz y por el manejo de errores de conexión.

