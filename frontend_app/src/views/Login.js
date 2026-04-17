const Login = {
    render: () => {
        return `
            <div class="flex-1 flex items-center justify-center p-6 animate-fade-in">
                <div class="glass-card w-full max-w-md p-10 rounded-[2rem] border-white/10 relative overflow-hidden">
                    <!-- Subtle inner glow -->
                    <div class="absolute -top-24 -right-24 w-48 h-48 bg-blue-500/10 rounded-full blur-[60px]"></div>
                    
                    <div class="relative z-10">
                        <div class="text-center mb-10">
                            <h1 class="text-4xl font-display font-bold text-gradient mb-2">Benvenuto</h1>
                            <p class="text-gray-400 font-light">Accedi al tuo account sicuro</p>
                        </div>

                        <form id="login-form" class="space-y-6">
                            <div class="space-y-2">
                                <label class="text-sm font-medium text-gray-300 ml-1">Username</label>
                                <input type="text" id="username" name="username" required
                                    class="glass-input w-full px-5 py-4 rounded-xl text-white placeholder-gray-500"
                                    placeholder="Inserisci il tuo username">
                            </div>

                            <div class="space-y-2">
                                <label class="text-sm font-medium text-gray-300 ml-1">Password</label>
                                <input type="password" id="password" name="password" required
                                    class="glass-input w-full px-5 py-4 rounded-xl text-white placeholder-gray-500"
                                    placeholder="••••••••">
                            </div>

                            <div id="error-message" class="hidden text-red-400 text-sm bg-red-500/10 p-3 rounded-lg border border-red-500/20">
                                Credenziali non valide. Riprova.
                            </div>

                            <button type="submit" id="login-btn"
                                class="w-full bg-blue-600 hover:bg-blue-500 text-white font-semibold py-4 rounded-xl transition-all duration-300 transform active:scale-[0.98] shadow-lg shadow-blue-500/20 mt-4">
                                Accedi
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        `;
    },
    after_render: (router) => {
        const form = document.getElementById('login-form');
        const loginBtn = document.getElementById('login-btn');
        const errorMsg = document.getElementById('error-message');

        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            // Visual feedback
            loginBtn.disabled = true;
            loginBtn.innerHTML = '<div class="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin mx-auto"></div>';
            errorMsg.classList.add('hidden');

            const params = new URLSearchParams();
            params.append('username', document.getElementById('username').value);
            params.append('password', document.getElementById('password').value);

            try {
                // Post to Spring standard /login endpoint
                await router.api.post('/login', params);
                
                // After successful login, redirect to root and let router decide
                window.location.hash = '#/';
            } catch (error) {
                console.error('Login failed', error);
                errorMsg.classList.remove('hidden');
                loginBtn.disabled = false;
                loginBtn.textContent = 'Accedi';
            }
        });
    }
};

export default Login;
