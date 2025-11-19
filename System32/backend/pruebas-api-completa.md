# Documentación Completa de Pruebas API

## Configuración Inicial

**Base URL:** `http://localhost:8080`

**Usuarios de Prueba:**
- Admin: `admin` / `admin123`
- Coordinador: `coordinador` / `coord123`

## Variables de Entorno en Postman

Crear las siguientes variables en tu entorno de Postman:
- `baseUrl`: `http://localhost:8080`
- `token`: (se guardará automáticamente después del login)
- `userId`: ID del usuario
- `categoryId`: ID de la categoría
- `subcategoryId`: ID de la subcategoría
- `productId`: ID del producto

---

# 1. AUTENTICACIÓN

## 1.1. Login como Admin

**Método:** `POST`  
**URL:** `{{baseUrl}}/api/auth/login`  
**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has token", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.token).to.be.a('string');
    pm.environment.set("token", jsonData.token);
});
```

**Respuesta Esperada:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@app.com",
  "role": "ADMIN"
}
```

---

## 1.2. Login como Coordinador

**Método:** `POST`  
**URL:** `{{baseUrl}}/api/auth/login`  
**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "username": "coordinador",
  "password": "coord123"
}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has coordinator role", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.role).to.eql('COORDINADOR');
    pm.environment.set("token", jsonData.token);
});
```

---

# 2. USUARIOS

## 2.1. Listar Todos los Usuarios

**Método:** `GET`  
**URL:** `{{baseUrl}}/api/users`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response is an array", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('array');
});
```

**Respuesta Esperada:**
```json
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@app.com",
    "role": "ADMIN",
    "active": true,
    "createdAt": "2025-11-14T10:30:00"
  }
]
```

---

## 2.2. Obtener Usuario por ID

**Método:** `GET`  
**URL:** `{{baseUrl}}/api/users/{{userId}}`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has user data", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.id).to.be.a('number');
    pm.expect(jsonData.username).to.be.a('string');
});
```

---

## 2.3. Crear Usuario (Solo ADMIN)

**Método:** `POST`  
**URL:** `{{baseUrl}}/api/users`  
**Headers:**
```
Authorization: Bearer {{token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "username": "nuevouser",
  "password": "password123",
  "email": "nuevouser@app.com",
  "role": "COORDINADOR",
  "active": true
}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

pm.test("Response has created user", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.id).to.be.a('number');
    pm.environment.set("userId", jsonData.id);
});
```

---

## 2.4. Actualizar Usuario (Solo ADMIN)

**Método:** `PUT`  
**URL:** `{{baseUrl}}/api/users/{{userId}}`  
**Headers:**
```
Authorization: Bearer {{token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "username": "usuariomodificado",
  "email": "modificado@app.com",
  "role": "COORDINADOR",
  "active": true
}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("User was updated", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.username).to.eql('usuariomodificado');
});
```

---

## 2.5. Eliminar Usuario (Solo ADMIN)

**Método:** `DELETE`  
**URL:** `{{baseUrl}}/api/users/{{userId}}`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has success message", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.message).to.include('eliminado');
});
```

---

# 3. CATEGORÍAS

## 3.1. Listar Todas las Categorías

**Método:** `GET`  
**URL:** `{{baseUrl}}/api/categories`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response is an array", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('array');
});

pm.test("Categories have required fields", function () {
    var jsonData = pm.response.json();
    if (jsonData.length > 0) {
        pm.expect(jsonData[0]).to.have.property('id');
        pm.expect(jsonData[0]).to.have.property('name');
        pm.expect(jsonData[0]).to.have.property('description');
        pm.expect(jsonData[0]).to.have.property('active');
    }
});
```

**Respuesta Esperada:**
```json
[
  {
    "id": 1,
    "name": "Electrónica",
    "description": "Productos electrónicos",
    "active": true
  }
]
```

---

## 3.2. Obtener Categoría por ID

**Método:** `GET`  
**URL:** `{{baseUrl}}/api/categories/{{categoryId}}`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has category data", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.id).to.be.a('number');
    pm.expect(jsonData.name).to.be.a('string');
});
```

**Respuesta Esperada:**
```json
{
  "id": 1,
  "name": "Electrónica",
  "description": "Productos electrónicos",
  "active": true
}
```

---

## 3.3. Crear Categoría

**Método:** `POST`  
**URL:** `{{baseUrl}}/api/categories`  
**Headers:**
```
Authorization: Bearer {{token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "name": "Hogar",
  "description": "Productos para el hogar",
  "active": true
}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has created category", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.id).to.be.a('number');
    pm.expect(jsonData.name).to.eql('Hogar');
    pm.environment.set("categoryId", jsonData.id);
});
```

**Respuesta Esperada:**
```json
{
  "id": 2,
  "name": "Hogar",
  "description": "Productos para el hogar",
  "active": true
}
```

---

## 3.4. Actualizar Categoría

**Método:** `PUT`  
**URL:** `{{baseUrl}}/api/categories/{{categoryId}}`  
**Headers:**
```
Authorization: Bearer {{token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "name": "Hogar y Decoración",
  "description": "Productos para el hogar y decoración",
  "active": true
}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Category was updated", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.name).to.eql('Hogar y Decoración');
});
```

---

## 3.5. Eliminar Categoría (Solo ADMIN)

**Método:** `DELETE`  
**URL:** `{{baseUrl}}/api/categories/{{categoryId}}`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has success message", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.message).to.include('eliminada');
});
```

**Respuesta Esperada:**
```json
{
  "message": "Categoría eliminada exitosamente"
}
```

---

# 4. SUBCATEGORÍAS

## 4.1. Listar Todas las Subcategorías

**Método:** `GET`  
**URL:** `{{baseUrl}}/api/subcategories`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response is an array", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('array');
});
```

**Respuesta Esperada:**
```json
[
  {
    "id": 1,
    "name": "Celulares",
    "description": "Teléfonos móviles",
    "active": true,
    "category": {
      "id": 1,
      "name": "Electrónica",
      "description": "Productos electrónicos",
      "active": true
    }
  }
]
```

---

## 4.2. Listar Subcategorías por Categoría

**Método:** `GET`  
**URL:** `{{baseUrl}}/api/subcategories/category/{{categoryId}}`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response is an array", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('array');
});

pm.test("All subcategories belong to the category", function () {
    var jsonData = pm.response.json();
    var categoryId = pm.environment.get("categoryId");
    jsonData.forEach(function(subcategory) {
        pm.expect(subcategory.category.id).to.eql(parseInt(categoryId));
    });
});
```

---

## 4.3. Obtener Subcategoría por ID

**Método:** `GET`  
**URL:** `{{baseUrl}}/api/subcategories/{{subcategoryId}}`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has subcategory data", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.id).to.be.a('number');
    pm.expect(jsonData.name).to.be.a('string');
    pm.expect(jsonData.category).to.be.an('object');
});
```

---

## 4.4. Crear Subcategoría

**Método:** `POST`  
**URL:** `{{baseUrl}}/api/subcategories`  
**Headers:**
```
Authorization: Bearer {{token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "name": "Laptops",
  "description": "Computadoras portátiles",
  "active": true,
  "category": {
    "id": 1
  }
}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has created subcategory", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.id).to.be.a('number');
    pm.expect(jsonData.name).to.eql('Laptops');
    pm.environment.set("subcategoryId", jsonData.id);
});
```

**Respuesta Esperada:**
```json
{
  "id": 2,
  "name": "Laptops",
  "description": "Computadoras portátiles",
  "active": true,
  "category": {
    "id": 1,
    "name": "Electrónica",
    "description": "Productos electrónicos",
    "active": true
  }
}
```

---

## 4.5. Actualizar Subcategoría

**Método:** `PUT`  
**URL:** `{{baseUrl}}/api/subcategories/{{subcategoryId}}`  
**Headers:**
```
Authorization: Bearer {{token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "name": "Laptops y Notebooks",
  "description": "Computadoras portátiles de todas las marcas",
  "active": true,
  "category": {
    "id": 1
  }
}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Subcategory was updated", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.name).to.eql('Laptops y Notebooks');
});
```

---

## 4.6. Eliminar Subcategoría (Solo ADMIN)

**Método:** `DELETE`  
**URL:** `{{baseUrl}}/api/subcategories/{{subcategoryId}}`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has success message", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.message).to.include('eliminada');
});
```

**Respuesta Esperada:**
```json
{
  "message": "SubCategoria eliminada con exito"
}
```

---

# 5. PRODUCTOS

## 5.1. Listar Todos los Productos

**Método:** `GET`  
**URL:** `{{baseUrl}}/api/products`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response is an array", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('array');
});

pm.test("Products have required fields", function () {
    var jsonData = pm.response.json();
    if (jsonData.length > 0) {
        pm.expect(jsonData[0]).to.have.property('id');
        pm.expect(jsonData[0]).to.have.property('name');
        pm.expect(jsonData[0]).to.have.property('price');
        pm.expect(jsonData[0]).to.have.property('stock');
        pm.expect(jsonData[0]).to.have.property('category');
        pm.expect(jsonData[0]).to.have.property('subcategory');
    }
});
```

**Respuesta Esperada:**
```json
[
  {
    "id": 1,
    "name": "iPhone 15 Pro",
    "description": "Smartphone Apple última generación",
    "price": 999.99,
    "stock": 50,
    "active": true,
    "category": {
      "id": 1,
      "name": "Electrónica",
      "description": "Productos electrónicos",
      "active": true
    },
    "subcategory": {
      "id": 1,
      "name": "Celulares",
      "description": "Teléfonos móviles",
      "active": true
    }
  }
]
```

---

## 5.2. Listar Productos por Categoría

**Método:** `GET`  
**URL:** `{{baseUrl}}/api/products/category/{{categoryId}}`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response is an array", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('array');
});

pm.test("All products belong to the category", function () {
    var jsonData = pm.response.json();
    var categoryId = pm.environment.get("categoryId");
    jsonData.forEach(function(product) {
        pm.expect(product.category.id).to.eql(parseInt(categoryId));
    });
});
```

---

## 5.3. Listar Productos por Subcategoría

**Método:** `GET`  
**URL:** `{{baseUrl}}/api/products/subcategory/{{subcategoryId}}`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response is an array", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('array');
});

pm.test("All products belong to the subcategory", function () {
    var jsonData = pm.response.json();
    var subcategoryId = pm.environment.get("subcategoryId");
    jsonData.forEach(function(product) {
        pm.expect(product.subcategory.id).to.eql(parseInt(subcategoryId));
    });
});
```

---

## 5.4. Obtener Producto por ID

**Método:** `GET`  
**URL:** `{{baseUrl}}/api/products/{{productId}}`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has product data", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.id).to.be.a('number');
    pm.expect(jsonData.name).to.be.a('string');
    pm.expect(jsonData.price).to.be.a('number');
});
```

---

## 5.5. Crear Producto

**Método:** `POST`  
**URL:** `{{baseUrl}}/api/products`  
**Headers:**
```
Authorization: Bearer {{token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "name": "Samsung Galaxy S24",
  "description": "Smartphone Samsung última generación",
  "price": 899.99,
  "stock": 30,
  "active": true,
  "category": {
    "id": 1
  },
  "subcategory": {
    "id": 1
  }
}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has created product", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.id).to.be.a('number');
    pm.expect(jsonData.name).to.eql('Samsung Galaxy S24');
    pm.expect(jsonData.price).to.eql(899.99);
    pm.environment.set("productId", jsonData.id);
});
```

**Respuesta Esperada:**
```json
{
  "id": 2,
  "name": "Samsung Galaxy S24",
  "description": "Smartphone Samsung última generación",
  "price": 899.99,
  "stock": 30,
  "active": true,
  "category": {
    "id": 1,
    "name": "Electrónica",
    "description": "Productos electrónicos",
    "active": true
  },
  "subcategory": {
    "id": 1,
    "name": "Celulares",
    "description": "Teléfonos móviles",
    "active": true
  }
}
```

---

## 5.6. Actualizar Producto

**Método:** `PUT`  
**URL:** `{{baseUrl}}/api/products/{{productId}}`  
**Headers:**
```
Authorization: Bearer {{token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "name": "Samsung Galaxy S24 Ultra",
  "description": "Smartphone Samsung última generación - Versión Ultra",
  "price": 1199.99,
  "stock": 25,
  "active": true,
  "category": {
    "id": 1
  },
  "subcategory": {
    "id": 1
  }
}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Product was updated", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.name).to.eql('Samsung Galaxy S24 Ultra');
    pm.expect(jsonData.price).to.eql(1199.99);
});
```

---

## 5.7. Eliminar Producto (Solo ADMIN)

**Método:** `DELETE`  
**URL:** `{{baseUrl}}/api/products/{{productId}}`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has success message", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.message).to.include('eliminado');
});
```

**Respuesta Esperada:**
```json
{
  "message": "Producto eliminado exitosamente"
}
```

---

# 6. ESTADÍSTICAS

## 6.1. Obtener Estadísticas Generales

**Método:** `GET`  
**URL:** `{{baseUrl}}/api/stats`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has stats", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('totalUsers');
    pm.expect(jsonData).to.have.property('totalCategories');
    pm.expect(jsonData).to.have.property('totalSubcategories');
    pm.expect(jsonData).to.have.property('totalProducts');
});

pm.test("All stats are numbers", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.totalUsers).to.be.a('number');
    pm.expect(jsonData.totalCategories).to.be.a('number');
    pm.expect(jsonData.totalSubcategories).to.be.a('number');
    pm.expect(jsonData.totalProducts).to.be.a('number');
});
```

**Respuesta Esperada:**
```json
{
  "totalUsers": 2,
  "totalCategories": 5,
  "totalSubcategories": 12,
  "totalProducts": 45
}
```

---

# FLUJO DE PRUEBA COMPLETO

## Orden Recomendado de Ejecución:

### 1. Autenticación
1. Login como Admin (1.1)
2. Login como Coordinador (1.2)

### 2. Gestión de Usuarios (Solo como ADMIN)
3. Listar Usuarios (2.1)
4. Crear Usuario (2.3)
5. Obtener Usuario por ID (2.2)
6. Actualizar Usuario (2.4)
7. Eliminar Usuario (2.5)

### 3. Gestión de Categorías
8. Listar Categorías (3.1)
9. Crear Categoría (3.3)
10. Obtener Categoría por ID (3.2)
11. Actualizar Categoría (3.4)

### 4. Gestión de Subcategorías
12. Listar Subcategorías (4.1)
13. Crear Subcategoría (4.4)
14. Listar Subcategorías por Categoría (4.2)
15. Obtener Subcategoría por ID (4.3)
16. Actualizar Subcategoría (4.5)

### 5. Gestión de Productos
17. Listar Productos (5.1)
18. Crear Producto (5.5)
19. Listar Productos por Categoría (5.2)
20. Listar Productos por Subcategoría (5.3)
21. Obtener Producto por ID (5.4)
22. Actualizar Producto (5.6)

### 6. Estadísticas
23. Obtener Estadísticas (6.1)

### 7. Eliminaciones (Solo como ADMIN)
24. Eliminar Producto (5.7)
25. Eliminar Subcategoría (4.6)
26. Eliminar Categoría (3.5)

---

# PERMISOS POR ROL

| Recurso | Acción | ADMIN | COORDINADOR |
|---------|--------|-------|-------------|
| **Usuarios** | Listar | ✅ | ✅ |
| | Ver | ✅ | ✅ |
| | Crear | ✅ | ❌ |
| | Actualizar | ✅ | ❌ |
| | Eliminar | ✅ | ❌ |
| **Categorías** | Listar | ✅ | ✅ |
| | Ver | ✅ | ✅ |
| | Crear | ✅ | ✅ |
| | Actualizar | ✅ | ✅ |
| | Eliminar | ✅ | ❌ |
| **Subcategorías** | Listar | ✅ | ✅ |
| | Ver | ✅ | ✅ |
| | Crear | ✅ | ✅ |
| | Actualizar | ✅ | ✅ |
| | Eliminar | ✅ | ❌ |
| **Productos** | Listar | ✅ | ✅ |
| | Ver | ✅ | ✅ |
| | Crear | ✅ | ✅ |
| | Actualizar | ✅ | ✅ |
| | Eliminar | ✅ | ❌ |
| **Estadísticas** | Ver | ✅ | ✅ |

---

# CÓDIGOS DE ESTADO HTTP

- `200 OK`: Operación exitosa
- `201 Created`: Recurso creado exitosamente
- `400 Bad Request`: Error en los datos enviados
- `401 Unauthorized`: Token no proporcionado o inválido
- `403 Forbidden`: Sin permisos para la operación
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error del servidor

---

# VALIDACIONES IMPORTANTES

## Categorías
- El nombre debe ser único
- El campo `active` permite desactivar categorías sin eliminarlas
- Al eliminar una categoría, se eliminan en cascada sus subcategorías y productos

## Subcategorías
- Deben pertenecer a una categoría existente
- El nombre no necesariamente es único (puede repetirse en diferentes categorías)
- Al eliminar una subcategoría, se eliminan en cascada sus productos

## Productos
- Deben pertenecer a una categoría y subcategoría existentes
- El precio debe ser mayor a 0
- El stock puede ser 0 o mayor
- El campo `active` permite desactivar productos sin eliminarlos

## Usuarios
- El username debe ser único
- El password no se devuelve en las respuestas (campo `@JsonIgnore`)
- Los roles disponibles son: `ADMIN` y `COORDINADOR`
- Todas las fechas se manejan en formato ISO-8601

---

# NOTAS ADICIONALES

1. **Autenticación JWT**: Todos los endpoints (excepto `/api/auth/login`) requieren un token JWT válido en el header `Authorization: Bearer {token}`.

2. **CORS**: La API está configurada con `@CrossOrigin(origins = "*")`, permitiendo peticiones desde cualquier origen.

3. **Relaciones**:
   - Categoría → Subcategorías (1:N)
   - Subcategoría → Productos (1:N)
   - Categoría → Productos (1:N)

4. **Cascada**: Las eliminaciones siguen el patrón en cascada:
   - Eliminar Categoría → Elimina Subcategorías → Elimina Productos
   - Eliminar Subcategoría → Elimina Productos

5. **Formato de Fechas**: Todas las fechas se devuelven en formato ISO-8601 (ej: `2025-11-18T14:30:00`).

6. **Paginación**: Actualmente no está implementada. Todos los endpoints devuelven todos los registros.

7. **Búsqueda y Filtrado**: No hay endpoints de búsqueda implementados actualmente.

---

# EJEMPLOS DE ERRORES COMUNES

## Error 401 - No autenticado
```json
{
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource"
}
```

## Error 403 - Sin permisos
```json
{
  "message": "No tienes permisos para realizar esta operación"
}
```

## Error 404 - Recurso no encontrado
```json
{
  "error": "Not Found",
  "message": "Producto no encontrado con id: 999"
}
```

## Error 400 - Datos inválidos
```json
{
  "error": "Bad Request",
  "message": "El nombre de la categoría es obligatorio"
}
```

## Error 500 - Error del servidor
```json
{
  "error": "Internal Server Error",
  "message": "Ha ocurrido un error inesperado"
}
```
