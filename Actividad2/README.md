# 📝 Notes & Categories App - Actividad 2

## 📖 Descripción
Aplicación Android desarrollada en **Java** bajo el patrón de arquitectura **MVC**. 
Esta actividad se centra en el manejo de **Relaciones 1:N (Uno a Muchos)** en bases de datos locales usando **Room**.

El sistema permite crear Categorías y agregar múltiples Notas asociadas a ellas. Además, implementa consultas avanzadas para búsqueda en tiempo real y filtrado dinámico.

### Características Principales
* **Relaciones SQL:** Implementación de `Foreign Key` entre Categorías y Notas (1:N).
* **Búsqueda Avanzada:** Uso de operadores `LIKE` para buscar texto dentro del título o contenido de la nota.
* **Filtrado Dinámico:** Spinner para visualizar notas solo de una categoría específica.
* **Gestión de Categorías:** Posibilidad de crear nuevas categorías personalizadas.
* **Interfaz Intuitiva:** Listado limpio que muestra el nombre de la categoría en cada nota.

---

## 🛠️ Instalación y Ejecución

1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/Kanchiz/TareasComputoMovil.git (https://github.com/Kanchiz/TareasComputoMovil)
    ```
2.  **Abrir en Android Studio:**
    * Navegar a la carpeta `Actividad 2` (o el nombre que le hayas puesto).
3.  **Ejecutar:**
    * Sincronizar Gradle y ejecutar en Emulador (API 26+).

---

## 🗄️ Estructura de la Base de Datos

La base de datos `notes_app_db` consta de dos tablas relacionadas:

### Tabla: `categories`
| Columna | Tipo | Descripción |
| :--- | :--- | :--- |
| `category_id` | INT | Primary Key (Auto-generada) |
| `category_name`| TEXT | Nombre de la categoría |

### Tabla: `notes`
| Columna | Tipo | Descripción |
| :--- | :--- | :--- |
| `note_id` | INT | Primary Key |
| `note_title` | TEXT | Título de la nota |
| `note_content` | TEXT | Contenido extenso |
| `created_at` | TEXT | Fecha de creación |
| `category_id` | INT | **Foreign Key** (Relación con categories) |

> **Nota:** La relación tiene configurado `ON DELETE CASCADE`. Si se borra una categoría, se borran sus notas.

---

## 📸 Capturas de Pantalla

### 1. Pantalla Principal (Buscador y Filtro)
> *Se observan las notas con su categoría "traducida" de ID a Nombre.*

![Pantalla Principal](./screenshots/main_notes.png)

### 2. Crear Nota y Categoría
> *Formulario para agregar nota y diálogo para nueva categoría.*

![Crear Nota](./screenshots/add_note.png)

---

**Materia:** Cómputo Móvil
**Arquitectura:** MVC (Model-View-Controller)
