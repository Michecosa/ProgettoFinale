const DashboardUser = {
    render: (user) => {
        return `
            <div class="flex-1 flex flex-col p-8 animate-fade-in max-w-6xl mx-auto w-full">
                <!-- Header -->
                <header class="flex justify-between items-center mb-12">
                    <div>
                        <h1 class="text-3xl font-display font-extrabold text-gradient">Dashboard</h1>
                        <p class="text-gray-400">Bentornato nella tua area protetta</p>
                    </div>
                    <button id="logout-btn" class="glass-card px-6 py-2.5 rounded-xl hover:bg-white/10 transition-colors flex items-center gap-2 border-white/5">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                        </svg>
                        Logout
                    </button>
                </header>

                <!-- Welcome Card -->
                <div class="glass-card p-10 rounded-[2.5rem] relative overflow-hidden border-white/10 glow-hover transition-all duration-500">
                    <div class="absolute -top-12 -right-12 w-64 h-64 bg-blue-600/10 rounded-full blur-[80px]"></div>
                    <div class="relative z-10 flex flex-col md:flex-row items-center gap-8">
                        <div class="w-24 h-24 rounded-full bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center text-3xl font-bold border-4 border-white/10 shadow-2xl">
                            ${user.username.charAt(0).toUpperCase()}
                        </div>
                        <div class="text-center md:text-left">
                            <h2 class="text-4xl font-display font-bold mb-2">Ciao, ${user.username}!</h2>
                            <p class="text-gray-400 max-w-md">Siamo felici di rivederti. Il tuo account è attivo e sicuro grazie alla nostra protezione crittografica avanzata.</p>
                        </div>
                    </div>
                </div>

                <!-- Simple Content Section (Placeholder) -->
                <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mt-10">
                    <div class="glass-card p-6 rounded-2xl border-white/5">
                        <h3 class="font-semibold mb-2">Accessi Recenti</h3>
                        <p class="text-sm text-gray-400">Nessuna attività insolita rilevata negli ultimi 30 giorni.</p>
                    </div>
                    <div class="glass-card p-6 rounded-2xl border-white/5">
                        <h3 class="font-semibold mb-2">Sicurezza</h3>
                        <p class="text-sm text-gray-400 text-green-400 flex items-center gap-2">
                            <span class="w-2 h-2 bg-green-400 rounded-full animate-pulse"></span> Sistema Protetto
                        </p>
                    </div>
                    <div class="glass-card p-6 rounded-2xl border-white/5">
                        <h3 class="font-semibold mb-2">Notifiche</h3>
                        <p class="text-sm text-gray-400">Non hai nuovi messaggi nel tuo inbox.</p>
                    </div>
                </div>
            </div>
        `;
    },
    after_render: (router) => {
        document.getElementById('logout-btn').addEventListener('click', async () => {
            try {
                await router.api.post('/logout');
                window.location.hash = '#/login';
            } catch (error) {
                console.error('Logout failed', error);
                // Fallback: reload page
                window.location.reload();
            }
        });
    }
};

export default DashboardUser;
