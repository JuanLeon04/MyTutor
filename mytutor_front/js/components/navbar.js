// ✅ Definición local de sendRequest para evitar dependencia con api.js
function sendRequest(endPoint, method, data) {
    // ✅ Obtener la URL desde config.js (guardada en localStorage)
    const url = localStorage.getItem('apiUrl') || 'http://10.14.101.98:8081'; 
    
    console.log(`📡 Enviando ${method} a ${url + endPoint}`);
    
    let request = new XMLHttpRequest();
    request.open(method, url + endPoint);
    request.responseType = 'json';
    request.setRequestHeader('Content-Type', 'application/json');

    // Agregar el token si está guardado
    const token = localStorage.getItem('token');
    if (token) {
        request.setRequestHeader('Authorization', 'Bearer ' + token);
    }

    // Enviar datos si no es DELETE
    if (method === 'DELETE') {
        request.send();
    } else if (data) {
        request.send(JSON.stringify(data));
    } else {
        request.send();
    }
    
    return request;
}

// ✅ Función para extraer el rol directamente del token
function getRoleFromToken() {
    try {
        const token = localStorage.getItem('token');
        if (!token) {
            return null;
        }
        
        const payload = parseJwt(token);
        if (!payload) {
            return null;
        }
        
        // ✅ Usar el campo 'rol' directamente del token con conversión automática
        if (payload.rol) {
            const role = payload.rol;
            
            // ✅ Conversión automática y transparente
            switch(role) {
                case 'ADMIN':
                case 'ROLE_ADMIN':
                    return 'ROLE_ADMIN';
                case 'TUTOR':
                case 'ROLE_TUTOR':
                    console.log('🔄 Detectado TUTOR, convirtiendo a ROLE_TUTOR');
                    return 'ROLE_TUTOR';
                case 'ESTUDIANTE':
                case 'ROLE_ESTUDIANTE':
                    return 'ROLE_ESTUDIANTE';
                default:
                    // Si el rol ya está en formato ROLE_*, devolverlo tal como está
                    return role.startsWith('ROLE_') ? role : `ROLE_${role}`;
            }
        }
        
        return 'ROLE_ESTUDIANTE';
    } catch (e) {
        return null;
    }
}

// ✅ Renderizar navbar usando directamente el rol del token
function renderNavbar() {
    const authenticated = isAuthenticated();
    
    let admin = false;
    let tutor = false;
    let currentRole = "No autenticado";
    
    if (authenticated) {
        const roleFromToken = getRoleFromToken();
        currentRole = roleFromToken || "Sin rol";
        
        admin = currentRole === 'ROLE_ADMIN';
        tutor = currentRole === 'ROLE_TUTOR';
    }
    
    let navLinks = '';
    
    if (authenticated) {
        navLinks = `
            <a href="tutores.html">TUTORES</a>
            <a href="materias.html">MATERIAS</a>
            <a href="perfil.html">MI PERFIL</a>
        `;
        
        if (tutor) {
            navLinks += `<a href="tutor-perfil.html">PERFIL DE TUTOR</a>`;
        } else if (!admin) {
            navLinks += `<a href="convertir-tutor.html">SER TUTOR</a>`;
        }
        
        if (admin) {
            navLinks += `<a href="admin-usuarios.html">USUARIOS</a>`;
        }
        
        navLinks += `<a href="#" onclick="event.preventDefault(); logout();">CERRAR SESIÓN</a>`;
    } else {
        navLinks = `
            <a href="tutores.html">TUTORES</a>
            <a href="materias.html">MATERIAS</a>
            <a href="login.html">INICIAR SESIÓN</a>
            <a href="register.html">REGISTRARSE</a>
        `;
    }
    
    document.getElementById('navbar').innerHTML = `
        <nav class="navbar">
            <div class="navbar-container">
                <a href="index.html" class="navbar-brand">Sistema de Tutorías</a>
                <div class="navbar-links">${navLinks}</div>
            </div>
        </nav>
        
        ${authenticated ? `
            <div style="position: fixed; bottom: 10px; right: 10px; background: #333; color: white; padding: 5px 10px; font-size: 12px; border-radius: 3px; z-index: 9999; opacity: 0.8;">
                Rol: ${currentRole}
            </div>
        ` : ''}
    `;
}

function logout() {
    if (confirm('¿Deseas cerrar sesión?')) {
        localStorage.removeItem('token');
        localStorage.removeItem('userRole');
        localStorage.removeItem('isTutor');
        window.location.href = 'index.html';
    }
}

// ✅ Solo renderizar navbar (versión simplificada)
renderNavbar();
