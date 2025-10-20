// Función para decodificar JWT manualmente
function parseJwt(token) {
    if (!token) return null; // ✅ AGREGAR ESTA VALIDACIÓN
    
    try {
        const base64Url = token.split('.')[1];
        if (!base64Url) return null; // ✅ AGREGAR ESTA VALIDACIÓN
        
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error('Error al decodificar token:', e);
        return null;
    }
}

// Obtener token
function getToken() {
    return localStorage.getItem('token');
}

// Guardar token
function saveToken(token) {
    localStorage.setItem('token', token);
}

// Eliminar token
function removeToken() {
    localStorage.removeItem('token');
}

// Verificar si está autenticado
function isAuthenticated() {
    const token = localStorage.getItem('token');
    return token !== null && token !== undefined && token !== '';
}

// Obtener información del usuario desde el token
function getUserFromToken() {
    const token = localStorage.getItem('token');
    if (!token) return null;
    return parseJwt(token);
}

// Obtener rol del usuario
function getUserRole() {
    const user = getUserFromToken();
    if (!user) return null;
    return user.role || user.authorities?.[0]?.authority || user.authorities?.[0] || null;
}

// Obtener ID del usuario
function getUserId() {
    const user = getUserFromToken();
    if (!user) {
        console.warn('⚠️ No se pudo obtener usuario desde token');
        return null;
    }
    
    // Intentar diferentes campos comunes en JWT de Spring Boot
    const userId = user.id || 
                   user.userId || 
                   user.sub || 
                   user.user_id ||
                   user.jti;
    
    console.log('🔍 Buscando ID en token. Campos disponibles:', Object.keys(user));
    console.log('✅ ID encontrado:', userId);
    
    return userId;
}

// Verificar roles con validación
function isAdmin() {
    if (!isAuthenticated()) {
        console.log('❌ No está autenticado');
        return false;
    }
    
    const token = localStorage.getItem('token');
    if (!token) return false;
    
    try {
        const payload = parseJwt(token);
        console.log('🔍 Verificando admin. Token payload:', payload);
        console.log('🔍 Username:', payload.sub);
        console.log('🔍 Rol:', payload.rol);
        
        // ✅ ADMIN = ROLE_ADMIN (equivalencia automática)
        const isAdminUser = payload.sub === 'admin' || payload.rol === 'ADMIN' || payload.rol === 'ROLE_ADMIN';
        
        console.log(isAdminUser ? '✅ Usuario ES administrador' : '❌ Usuario NO es administrador');
        
        return isAdminUser;
    } catch (e) {
        console.error('❌ Error al verificar admin:', e);
        return false;
    }
}

// ✅ Obtener rol desde el backend
async function getMyRoleFromBackend() {
    if (!isAuthenticated()) {
        console.log('❌ getMyRoleFromBackend: No autenticado');
        return null;
    }
    
    return new Promise((resolve) => {
        console.log('📤 Enviando GET a /api/usuario/getMyRole...');
        
        const request = sendRequest('/api/usuario/getMyRole', 'GET');
        
        request.onload = function() {
            console.log('📡 Respuesta recibida - Status:', request.status);
            
            if (request.status === 200) {
                const role = request.response; // ROLE_ADMIN, ROLE_TUTOR o ROLE_ESTUDIANTE
                console.log('✅ Rol obtenido correctamente:', role);
                resolve(role);
            } else {
                console.log('❌ Error al obtener rol - Status:', request.status);
                console.log('❌ Respuesta:', request.response);
                resolve(null);
            }
        };
        
        request.onerror = function(error) {
            console.log('❌ Error de conexión al obtener rol:', error);
            resolve(null);
        };
    });
}

// ✅ Verificar si es tutor consultando el backend
async function isTutorFromBackend() {
    const role = await getMyRoleFromBackend();
    return role === 'ROLE_TUTOR';
}

// ✅ Verificar si es admin consultando el backend
async function isAdminFromBackend() {
    const role = await getMyRoleFromBackend();
    return role === 'ROLE_ADMIN';
}

function isTutor() {
    const token = localStorage.getItem('token');
    if (!token) {
        console.log('❌ No hay token');
        return false;
    }
    
    try {
        const payload = parseJwt(token);
        console.log('🔍 Token payload completo:', payload);
        console.log('🔍 Rol en token:', payload.rol);
        
        // ✅ TUTOR = ROLE_TUTOR (equivalencia automática)
        const isTutorUser = payload.rol === 'TUTOR' || payload.rol === 'ROLE_TUTOR';
        console.log(isTutorUser ? '✅ Usuario ES tutor (rol: ' + payload.rol + ')' : '❌ Usuario NO es tutor');
        
        return isTutorUser;
    } catch (e) {
        console.error('❌ Error al verificar rol de tutor:', e);
        return false;
    }
}

function isStudent() {
    if (!isAuthenticated()) return false;
    const role = getUserRole();
    return role === 'ESTUDIANTE' || role === 'ROLE_ESTUDIANTE';
}

// Redireccionar
function redirect(page) {
    window.location.href = page;
}

// Mostrar mensaje de error
function showError(elementId, message) {
    const errorDiv = document.getElementById(elementId);
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';
    }
}

// Ocultar mensaje de error
function hideError(elementId) {
    const errorDiv = document.getElementById(elementId);
    if (errorDiv) {
        errorDiv.style.display = 'none';
    }
}

// Mostrar mensaje de éxito
function showSuccess(elementId, message) {
    const successDiv = document.getElementById(elementId);
    if (successDiv) {
        successDiv.textContent = message;
        successDiv.style.display = 'block';
    }
}

// Formatear precio
function formatPrice(price) {
    return new Intl.NumberFormat('es-CO', { 
        style: 'currency', 
        currency: 'COP' 
    }).format(price);
}

// ✅ Verificar si el usuario tiene perfil de tutor (consulta al backend)
async function checkIfHasTutorProfile() {
    if (!isAuthenticated()) return false;
    
    return new Promise((resolve) => {
        const request = sendRequest('/api/tutor', 'GET');
        
        request.onload = function() {
            // Si devuelve 200, tiene perfil de tutor
            // Si devuelve 404, no tiene perfil de tutor
            resolve(request.status === 200);
        };
        
        request.onerror = function() {
            resolve(false);
        };
    });
}

// ✅ Verificar si es tutor - versión robusta
async function isTutorRobust() {
    // 1. Verificar desde localStorage
    const savedRole = localStorage.getItem('userRole');
    if (savedRole === 'ROLE_TUTOR') {
        return true;
    }
    
    // 2. Verificar desde el token
    const isTutorFromToken = isTutor();
    if (isTutorFromToken) {
        return true;
    }
    
    // 3. Verificar consultando si tiene perfil de tutor
    try {
        const request = sendRequest('/api/tutor', 'GET');
        
        return new Promise((resolve) => {
            request.onload = function() {
                // Si devuelve 200, tiene perfil de tutor
                const hasTutorProfile = request.status === 200;
                
                if (hasTutorProfile) {
                    localStorage.setItem('userRole', 'ROLE_TUTOR');
                    localStorage.setItem('isTutor', 'true');
                }
                
                resolve(hasTutorProfile);
            };
            
            request.onerror = function() {
                resolve(false);
            };
        });
    } catch (e) {
        return false;
    }
}
