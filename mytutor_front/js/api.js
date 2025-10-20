// Función principal para enviar peticiones
function sendRequest(endPoint, method, data) {
    const url = localStorage.getItem('apiUrl') || 'http://10.14.101.98:8081';
    
    console.log(`📡 Enviando ${method} a ${url + endPoint}`);
    
    let request = new XMLHttpRequest();
    request.open(method, url + endPoint);
    request.responseType = 'json';
    request.setRequestHeader('Content-Type', 'application/json');

    const token = localStorage.getItem('token');
    if (token) {
        request.setRequestHeader('Authorization', 'Bearer ' + token);
    }

    if (method === 'DELETE') {
        request.send();
    } else if (data) {
        request.send(JSON.stringify(data));
    } else {
        request.send();
    }
    
    return request;
}

// Función especial para login que devuelve texto
function sendRequestText(endPoint, method, data) {
    const url = localStorage.getItem('apiUrl') || 'http://10.14.101.98:8081';
    let request = new XMLHttpRequest();
    request.open(method, url + endPoint);
    request.responseType = 'text';
    request.setRequestHeader('Content-Type', 'application/json');

    request.send(data ? JSON.stringify(data) : data);
    return request;
}

// Wrapper para convertir sendRequest en Promise
function fetchAPI(endpoint, options = {}) {
    return new Promise((resolve, reject) => {
        const method = options.method || 'GET';
        const data = options.body ? JSON.parse(options.body) : null;
        
        const request = sendRequest(endpoint, method, data);
        
        request.onload = function() {
            if (request.status === 401 || request.status === 403) {
                localStorage.removeItem('token');
                window.location.href = 'login.html';
                reject(new Error('Sesión expirada'));
                return;
            }
            
            if (request.status === 204) {
                resolve(null);
                return;
            }
            
            if (request.status >= 200 && request.status < 300) {
                resolve(request.response);
            } else {
                reject(new Error(request.response?.message || `Error ${request.status}`));
            }
        };
        
        request.onerror = function() {
            reject(new Error('Error de red'));
        };
    });
}

// API de Autenticación
const AuthAPI = {
    login: (credentials) => {
        return fetchAPI('/auth/login', {
            method: 'POST',
            body: JSON.stringify(credentials)
        });
    },
    
    register: (userData) => {
        return fetchAPI('/auth/register', {
            method: 'POST',
            body: JSON.stringify(userData)
        });
    }
};

// API de Usuarios
const UsuarioAPI = {
    getAll: () => fetchAPI('/api/usuario/list'),
    
    getById: (id) => fetchAPI(`/api/usuario/${id}`),
    
    update: (userData) => {
        return fetchAPI('/api/usuario', {
            method: 'PUT',
            body: JSON.stringify(userData)
        });
    },
    
    delete: () => {
        return fetchAPI('/api/usuario', {
            method: 'DELETE'
        });
    },
    
    deleteById: (id) => {
        return fetchAPI(`/api/usuario/${id}`, {
            method: 'DELETE'
        });
    }
};

// API de Tutores - ÚNICA DECLARACIÓN
const TutorAPI = {
    getAll: () => fetchAPI('/api/tutor/list'),
    
    getById: (id) => fetchAPI(`/api/tutor/${id}`),
    
    getMyProfile: () => {
        return new Promise((resolve, reject) => {
            console.log('📥 TutorAPI.getMyProfile() - Consultando /api/tutor');
            
            const request = sendRequest('/api/tutor', 'GET');
            
            request.onload = function() {
                console.log('📡 GET /api/tutor - Status:', request.status);
                console.log('📡 Response:', request.response);
                
                if (request.status === 200) {
                    resolve(request.response);
                } else if (request.status === 403) {
                    reject(new Error('No tienes permisos de tutor. Intenta cerrar sesión e iniciar sesión nuevamente.'));
                } else if (request.status === 404) {
                    reject(new Error('No tienes perfil de tutor creado'));
                } else if (request.status === 401) {
                    reject(new Error('Tu sesión ha expirado. Por favor inicia sesión nuevamente.'));
                } else {
                    reject(new Error(`Error ${request.status}: No se pudo obtener el perfil de tutor`));
                }
            };
            
            request.onerror = () => reject(new Error('Error de conexión'));
        });
    },
    
    create: (tutorData) => {
        return fetchAPI('/api/tutor', {
            method: 'POST',
            body: JSON.stringify(tutorData)
        });
    },
    
    update: function(data) {
        return new Promise((resolve, reject) => {
            console.log('📤 TutorAPI.update() - Actualizando perfil...');
            
            const request = sendRequest('/api/tutor', 'PUT', data);
            
            request.onload = function() {
                console.log('📡 PUT /api/tutor - Status:', request.status);
                
                if (request.status === 200) {
                    resolve(request.response);
                } else if (request.status === 403) {
                    reject(new Error('No tienes permisos para actualizar este perfil'));
                } else if (request.status === 400) {
                    reject(new Error('Datos inválidos: Verifica los campos'));
                } else {
                    reject(new Error('Error al actualizar perfil de tutor'));
                }
            };
            
            request.onerror = () => reject(new Error('Error de conexión'));
        });
    },
    
    delete: () => {
        return fetchAPI('/api/tutor', {
            method: 'DELETE'
        });
    },
    
    deleteById: (id) => {
        return fetchAPI(`/api/tutor/${id}`, {
            method: 'DELETE'
        });
    }
};

// API de Materias
const MateriaAPI = {
    getAll: () => fetchAPI('/api/materia/list'),
    
    getById: (id) => fetchAPI(`/api/materia/${id}`),
    
    create: (materiaData) => {
        return fetchAPI('/api/materia', {
            method: 'POST',
            body: JSON.stringify(materiaData)
        });
    },
    
    update: (materiaData) => {
        return fetchAPI('/api/materia', {
            method: 'PUT',
            body: JSON.stringify(materiaData)
        });
    },
    
    delete: (id) => {
        return fetchAPI(`/api/materia/${id}`, {
            method: 'DELETE'
        });
    }
};

// Función para verificar el rol manualmente desde la consola
function checkMyRole() {
    const request = sendRequest('/api/usuario/getMyRole', 'GET');
    
    request.onload = function() {
        console.group('🔍 Verificación manual de rol');
        console.log('📡 Status:', request.status);
        console.log('📡 Response:', request.response);
        
        if (request.status === 200) {
            console.log('✅ Tu rol es: %c' + request.response, 'color: green; font-weight: bold;');
            
            const token = localStorage.getItem('token');
            if (token) {
                const payload = parseJwt(token);
                console.log('🔍 Rol según token:', payload.rol);
            }
        } else {
            console.log('❌ Error al obtener rol');
        }
        console.groupEnd();
    };
    
    return "Verificando rol... revisa la consola";
}
