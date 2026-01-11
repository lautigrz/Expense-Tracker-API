# Expense Tracker API

API para la gestión de gastos personales. Permite a los usuarios registrarse, autenticarse y administrar sus gastos de manera segura.

Practica backend de [Roadmap.sh](https://roadmap.sh/projects/expense-tracker-api) "Expense Tracker API")

## 🚀 Características

- **Autenticación con JWT**  
  - Registro de usuarios  
  - Inicio de sesión con validación de token  

- **Gestión de gastos (CRUD)**  
  - Crear un nuevo gasto  
  - Listar gastos propios  
  - Actualizar un gasto existente  
  - Eliminar un gasto  

- **Filtros de gastos**  
  - Última semana  
  - Último mes  
  - Últimos 3 meses  
  - Personalizado (rango de fechas con inicio y fin)  

- **Categorías disponibles**  
  - Groceries (Comestibles)  
  - Leisure (Ocio)  
  - Electronics (Electrónica)  
  - Utilities (Servicios)  
  - Clothing (Ropa)  
  - Health (Salud)  
  - Others (Otros)  

## 📦 Tecnologías utilizadas
- Spring Boot  
- Spring Security + JWT  
- JPA/Hibernate  
- Base de datos relacional  

## 🔒 Seguridad
Todos los endpoints relacionados con gastos están protegidos con **JWT**, asegurando que cada usuario solo pueda acceder a sus propios datos.
