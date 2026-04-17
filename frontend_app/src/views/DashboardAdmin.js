const DashboardAdmin = {
    render: (user) => {
        return `
            <div class="flex-1 flex flex-col p-8 animate-fade-in max-w-6xl mx-auto w-full">
                <!-- Header -->
                <header class="flex justify-between items-center mb-12">
                    <div class="flex items-center gap-4">
                        <h1 class="text-3xl font-display font-extrabold text-gradient">Admin Center</h1>
                        <span class="bg-red-500/20 text-red-400 text-xs font-bold px-3 py-1 rounded-full border border-red-500/30 uppercase tracking-widest">Admin</span>
                    </div>
                    <button id="logout-btn" class="glass-card px-6 py-2.5 rounded-xl hover:bg-white/10 transition-colors flex items-center gap-2 border-white/5">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                        </svg>
                        Logout
                    </button>
                </header>

                <!-- Status Overview -->
                <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-10">
                    <div class="glass-card p-6 rounded-3xl border-white/10 glow-hover transition-all">
                        <span class="text-gray-400 text-sm">Utenti Totali</span>
                        <h4 class="text-3xl font-bold mt-1">1,284</h4>
                    </div>
                    <div class="glass-card p-6 rounded-3xl border-white/10 glow-hover transition-all">
                        <span class="text-gray-400 text-sm">Sessioni Attive</span>
                        <h4 class="text-3xl font-bold mt-1 text-green-400">42</h4>
                    </div>
                    <div class="glass-card p-6 rounded-3xl border-white/10 glow-hover transition-all">
                        <span class="text-gray-400 text-sm">Minacce Parate</span>
                        <h4 class="text-3xl font-bold mt-1 text-blue-400">12</h4>
                    </div>
                    <div class="glass-card p-6 rounded-3xl border-white/10 glow-hover transition-all">
                        <span class="text-gray-400 text-sm">Uptime Sistema</span>
                        <h4 class="text-3xl font-bold mt-1">99.9%</h4>
                    </div>
                </div>

                <!-- User Management Placeholder -->
                <div class="glass-card p-10 rounded-[2.5rem] border-white/10 relative overflow-hidden flex-1 flex flex-col">
                    <div class="absolute -bottom-24 -left-24 w-96 h-96 bg-purple-600/10 rounded-full blur-[100px]"></div>
                    
                    <div class="relative z-10 flex flex-col h-full">
                        <div class="flex justify-between items-center mb-8">
                            <h2 class="text-2xl font-display font-bold">Gestione Utenti</h2>
                            <button class="bg-white/10 hover:bg-white/20 text-sm font-medium px-4 py-2 rounded-lg transition-all">Esporta Dati</button>
                        </div>

                        <div class="flex-1 flex items-center justify-center border-2 border-dashed border-white/5 rounded-3xl bg-black/20">
                            <div style="margin-block: 3rem;"  class="text-center">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-gray-600 mx-auto mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                                </svg>
                                <p class="text-gray-500 italic">Database Management Module Loading...</p>
                            </div>
                        </div>
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
                window.location.reload();
            }
        });
    }
};

export default DashboardAdmin;
