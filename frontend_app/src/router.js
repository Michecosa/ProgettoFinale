import api from './api.js';
import Login from './views/Login.js';
import DashboardUser from './views/DashboardUser.js';
import DashboardAdmin from './views/DashboardAdmin.js';

const routes = {
    '/login': Login,
    '/dashboard/user': DashboardUser,
    '/dashboard/admin': DashboardAdmin,
};

class Router {
    constructor() {
        this.app = document.getElementById('app');
        this.api = api;
        window.addEventListener('hashchange', () => this.handleRoute());
        this.handleRoute();
    }

    async handleRoute() {
        const path = window.location.hash.slice(1) || '/';
        
        // Show loader while fetching user info
        this.showLoader();

        try {
            // Check authentication and role
            const userResponse = await this.api.get('/public/whoami');
            const userData = userResponse.data;

            if (path === '/login') {
                if (userData.authenticated) {
                    this.redirectToDashboard(userData);
                    return;
                }
                this.render(Login);
            } else {
                if (!userData.authenticated) {
                    window.location.hash = '#/login';
                    return;
                }
                
                // Root redirect to appropriate dashboard
                if (path === '/') {
                    this.redirectToDashboard(userData);
                    return;
                }

                // Protected route check
                if (path === '/dashboard/admin' && !userData.roles.includes('ROLE_ADMIN')) {
                    window.location.hash = '#/dashboard/user';
                    return;
                }

                const view = routes[path] || DashboardUser;
                this.render(view, userData);
            }
        } catch (error) {
            console.error('Routing error', error);
            if (path !== '/login') {
                window.location.hash = '#/login';
            } else {
                this.render(Login);
            }
        }
    }

    redirectToDashboard(user) {
        if (user.roles.includes('ROLE_ADMIN')) {
            window.location.hash = '#/dashboard/admin';
        } else {
            window.location.hash = '#/dashboard/user';
        }
    }

    showLoader() {
        this.app.innerHTML = `
            <div class="flex-1 flex items-center justify-center">
                <div class="w-12 h-12 border-4 border-blue-500/20 border-t-blue-500 rounded-full animate-spin"></div>
            </div>
        `;
    }

    render(view, data = null) {
        this.app.innerHTML = view.render(data);
        if (view.after_render) {
            view.after_render(this);
        }
    }
}

export default Router;
