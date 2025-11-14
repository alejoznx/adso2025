# Pruebas API - Gestión de Usuarios

## Configuración Inicial

**Base URL:** `http://localhost:8080`

**Usuarios de Prueba:**
- Admin: `admin` / `admin123`
- Coordinador: `coordinador` / `coord123`

## Variables de Entorno en Postman

Crear las siguientes variables en tu entorno de Postman:
- `baseUrl`: `http://localhost:8080`
- `token`: (se guardará automáticamente después del login)
- `userId`: (se guardará automáticamente después de crear un usuario)

---

## 1. Autenticación

### 1.1. Login como Admin

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
    pm.expect(jsonData.token.length).to.be.above(0);
    
    // Guardar token en variable de entorno
    pm.environment.set("token", jsonData.token);
});

pm.test("Response has user data", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.id).to.be.a('number');
    pm.expect(jsonData.username).to.eql('admin');
    pm.expect(jsonData.role).to.eql('ADMIN');
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

### 1.2. Login como Coordinador

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

### 1.3. Login con Credenciales Incorrectas

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
  "password": "wrongpassword"
}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 400", function () {
    pm.response.to.have.status(400);
});

pm.test("Response contains error message", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.error).to.exist;
});
```

---

## 2. Listar Usuarios

### 2.1. Listar Todos los Usuarios (Admin)

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

pm.test("Users have required fields", function () {
    var jsonData = pm.response.json();
    if (jsonData.length > 0) {
        pm.expect(jsonData[0]).to.have.property('id');
        pm.expect(jsonData[0]).to.have.property('username');
        pm.expect(jsonData[0]).to.have.property('email');
        pm.expect(jsonData[0]).to.have.property('role');
        pm.expect(jsonData[0]).to.not.have.property('password');
    }
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
  },
  {
    "id": 2,
    "username": "coordinador",
    "email": "coordinador@app.com",
    "role": "COORDINADOR",
    "active": true,
    "createdAt": "2025-11-14T10:30:00"
  }
]
```

---

### 2.2. Listar Usuarios Sin Token

**Método:** `GET`  
**URL:** `{{baseUrl}}/api/users`  
**Headers:** (sin Authorization)

**Tests (Scripts):**
```javascript
pm.test("Status code is 401 or 403", function () {
    pm.expect([401, 403]).to.include(pm.response.code);
});
```

---

## 3. Obtener Usuario por ID

### 3.1. Obtener Usuario Existente

**Método:** `GET`  
**URL:** `{{baseUrl}}/api/users/1`  
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
    pm.expect(jsonData.id).to.eql(1);
    pm.expect(jsonData.username).to.be.a('string');
    pm.expect(jsonData.email).to.be.a('string');
});
```

**Respuesta Esperada:**
```json
{
  "id": 1,
  "username": "admin",
  "email": "admin@app.com",
  "role": "ADMIN",
  "active": true,
  "createdAt": "2025-11-14T10:30:00"
}
```

---

### 3.2. Obtener Usuario No Existente

**Método:** `GET`  
**URL:** `{{baseUrl}}/api/users/9999`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 404", function () {
    pm.response.to.have.status(404);
});
```

---

## 4. Crear Usuario

### 4.1. Crear Usuario (Como Admin)

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
    pm.expect(jsonData.username).to.eql('nuevouser');
    pm.expect(jsonData.email).to.eql('nuevouser@app.com');
    
    // Guardar ID para pruebas posteriores
    pm.environment.set("userId", jsonData.id);
});
```

**Respuesta Esperada:**
```json
{
  "id": 3,
  "username": "nuevouser",
  "email": "nuevouser@app.com",
  "role": "COORDINADOR",
  "active": true,
  "createdAt": "2025-11-14T11:00:00"
}
```

---

### 4.2. Crear Usuario Como Coordinador (Sin Permisos)

**Método:** `POST`  
**URL:** `{{baseUrl}}/api/users`  
**Headers:**
```
Authorization: Bearer {{token}}
Content-Type: application/json
```

**Nota:** Primero hacer login como coordinador

**Body (JSON):**
```json
{
  "username": "otrouser",
  "password": "password123",
  "email": "otrouser@app.com",
  "role": "COORDINADOR",
  "active": true
}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 403", function () {
    pm.response.to.have.status(403);
});
```

---

### 4.3. Crear Usuario con Username Duplicado

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
  "username": "admin",
  "password": "password123",
  "email": "otro@app.com",
  "role": "COORDINADOR",
  "active": true
}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 400 or 500", function () {
    pm.expect([400, 500]).to.include(pm.response.code);
});
```

---

## 5. Actualizar Usuario

### 5.1. Actualizar Usuario (Como Admin)

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
    pm.expect(jsonData.email).to.eql('modificado@app.com');
});
```

**Respuesta Esperada:**
```json
{
  "id": 3,
  "username": "usuariomodificado",
  "email": "modificado@app.com",
  "role": "COORDINADOR",
  "active": true,
  "createdAt": "2025-11-14T11:00:00"
}
```

---

### 5.2. Actualizar Usuario Como Coordinador (Sin Permisos)

**Método:** `PUT`  
**URL:** `{{baseUrl}}/api/users/1`  
**Headers:**
```
Authorization: Bearer {{token}}
Content-Type: application/json
```

**Nota:** Primero hacer login como coordinador

**Body (JSON):**
```json
{
  "username": "adminmodificado",
  "email": "adminmod@app.com"
}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 403", function () {
    pm.response.to.have.status(403);
});

pm.test("Response contains error message", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.message).to.include('permiso');
});
```

---

### 5.3. Actualizar Contraseña de Usuario

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
  "password": "nuevapassword123"
}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

// Verificar que se puede hacer login con la nueva contraseña
// (esto se haría en una petición separada)
```

---

## 6. Eliminar Usuario

### 6.1. Eliminar Usuario (Como Admin)

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

**Respuesta Esperada:**
```json
{
  "message": "Usuario eliminado con exito"
}
```

---

### 6.2. Eliminar Usuario Como Coordinador (Sin Permisos)

**Método:** `DELETE`  
**URL:** `{{baseUrl}}/api/users/1`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Nota:** Primero hacer login como coordinador

**Tests (Scripts):**
```javascript
pm.test("Status code is 403", function () {
    pm.response.to.have.status(403);
});
```

---

### 6.3. Eliminar Usuario No Existente

**Método:** `DELETE`  
**URL:** `{{baseUrl}}/api/users/9999`  
**Headers:**
```
Authorization: Bearer {{token}}
```

**Tests (Scripts):**
```javascript
pm.test("Status code is 400 or 404", function () {
    pm.expect([400, 404]).to.include(pm.response.code);
});
```

---

## Flujo de Prueba Completo

### Orden Recomendado de Ejecución:

1. **Login como Admin** (1.1) → Guarda el token
2. **Listar Usuarios** (2.1) → Verifica usuarios existentes
3. **Crear Usuario** (4.1) → Guarda el userId
4. **Obtener Usuario por ID** (3.1) → Usa el userId guardado
5. **Actualizar Usuario** (5.1) → Modifica el usuario creado
6. **Eliminar Usuario** (6.1) → Elimina el usuario creado
7. **Login como Coordinador** (1.2) → Prueba permisos
8. **Intentar Crear Usuario como Coordinador** (4.2) → Debe fallar
9. **Intentar Actualizar como Coordinador** (5.2) → Debe fallar
10. **Intentar Eliminar como Coordinador** (6.2) → Debe fallar

---

## Notas Adicionales

### Permisos por Rol:

| Acción | ADMIN | COORDINADOR |
|--------|-------|-------------|
| Listar usuarios | ✅ | ✅ |
| Ver usuario | ✅ | ✅ |
| Crear usuario | ✅ | ❌ |
| Actualizar usuario | ✅ | ❌ |
| Eliminar usuario | ✅ | ❌ |

### Validaciones Importantes:

- El username debe ser único
- El password no se devuelve en las respuestas (campo `@JsonIgnore`)
- El campo `active` permite desactivar usuarios sin eliminarlos
- Los roles disponibles son: `ADMIN` y `COORDINADOR`
- Todas las fechas se manejan en formato ISO-8601

### Códigos de Estado HTTP:

- `200 OK`: Operación exitosa
- `201 Created`: Usuario creado exitosamente
- `400 Bad Request`: Error en los datos enviados
- `401 Unauthorized`: Token no proporcionado o inválido
- `403 Forbidden`: Sin permisos para la operación
- `404 Not Found`: Usuario no encontrado
- `500 Internal Server Error`: Error del servidor
