/**
 * Authentication & Shop logic for Final Project Shop
 */

declare global {
    interface Window {
        toggleAuth: (mode: 'login' | 'register') => void;
        addToCart: (productId: number) => void;
        removeFromCart: (productId: number) => void;
        clearCart: () => void;
        logout: () => void;
    }
}

interface Product {
    id: number;
    nome: string;
    prezzo: number;
    categorie?: any[];
}

interface CartItem {
    id: number;
    qtn: number;
    prodotto: Product;
}

interface Cart {
    id: number;
    items: CartItem[];
}

interface UserAuth {
    username: string;
    password: string;
    mail?: string;
}

type ToastType = 'success' | 'error' | 'info';

(() => {
    // Application State
    let products: Product[] = [];
    let currentCart: Cart | null = null;

    const getAuthHeaders = () => {
        const token = localStorage.getItem('jwt_token');
        return {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        };
    };

    /**
     * View Management
     */
    const showView = (view: 'auth' | 'shop') => {
        const authView = document.getElementById('auth-view');
        const shopView = document.getElementById('shop-view');
        const mainNav = document.getElementById('main-nav');

        if (view === 'shop') {
            authView?.classList.add('d-none');
            shopView?.classList.remove('d-none');
            mainNav?.classList.remove('d-none');
            initShop();
        } else {
            authView?.classList.remove('d-none');
            shopView?.classList.add('d-none');
            mainNav?.classList.add('d-none');
        }
    };

    /**
     * Shop Logic
     */
    const initShop = async () => {
        await fetchProducts();
        await fetchCart();
    };

    const fetchProducts = async () => {
        try {
            const response = await fetch('/api/products');
            if (response.ok) {
                products = await response.json();
                renderProducts();
            }
        } catch (err) {
            showToast('Errore nel caricamento prodotti', 'error');
        }
    };

    const renderProducts = () => {
        const grid = document.getElementById('products-grid');
        if (!grid) return;

        if (products.length === 0) {
            grid.innerHTML = '<div class="col-12 text-center text-white"><p>Nessun prodotto disponibile.</p></div>';
            return;
        }

        grid.innerHTML = products.map(p => `
            <div class="col-md-4 col-lg-3">
                <div class="product-card animate__animated animate__fadeInUp">
                    <div class="product-image-placeholder">
                        <i class="fas fa-box-open"></i>
                        <div class="price-tag">€ ${p.prezzo.toFixed(2)}</div>
                    </div>
                    <div class="product-body">
                        <div class="product-category">E-Commerce Item</div>
                        <h5 class="product-title">${p.nome}</h5>
                        <button class="btn btn-add-cart w-100" onclick="addToCart(${p.id})">
                            <i class="fas fa-cart-plus me-2"></i> Aggiungi
                        </button>
                    </div>
                </div>
            </div>
        `).join('');
    };

    /**
     * Cart Logic
     */
    const fetchCart = async () => {
        try {
            const response = await fetch('/api/carts/mine', { headers: getAuthHeaders() });
            if (response.ok) {
                currentCart = await response.json();
                renderCart();
            }
        } catch (err) {
            console.error('Errore fetch cart', err);
        }
    };

    const addToCart = async (productId: number) => {
        try {
            const response = await fetch(`/api/carts/add/${productId}?qtn=1`, {
                method: 'POST',
                headers: getAuthHeaders()
            });
            if (response.ok) {
                currentCart = await response.json();
                renderCart();
                showToast('Prodotto aggiunto al carrello!', 'success');
            }
        } catch (err) {
            showToast('Errore nell\'aggiunta al carrello', 'error');
        }
    };

    const removeFromCart = async (productId: number) => {
        try {
            const response = await fetch(`/api/carts/remove/${productId}`, {
                method: 'DELETE',
                headers: getAuthHeaders()
            });
            if (response.ok) {
                currentCart = await response.json();
                renderCart();
                showToast('Prodotto rimosso', 'info');
            }
        } catch (err) {
            showToast('Errore nella rimozione', 'error');
        }
    };

    const clearCart = async () => {
        try {
            const response = await fetch('/api/carts/clear', {
                method: 'DELETE',
                headers: getAuthHeaders()
            });
            if (response.ok) {
                currentCart = await response.json();
                renderCart();
                showToast('Carrello svuotato', 'info');
            }
        } catch (err) {
            showToast('Errore', 'error');
        }
    };

    const renderCart = () => {
        const container = document.getElementById('cart-items-container');
        const badge = document.getElementById('cart-badge');
        const totalEl = document.getElementById('cart-total');
        const checkoutBtn = document.getElementById('checkout-btn') as HTMLButtonElement;

        if (!container || !currentCart) return;

        const items = currentCart.items || [];

        // Update badge
        if (items.length > 0) {
            badge?.classList.remove('d-none');
            const totalItems = items.reduce((acc, item) => acc + item.qtn, 0);
            if (badge) badge.innerText = totalItems.toString();
            checkoutBtn.disabled = false;
        } else {
            badge?.classList.add('d-none');
            checkoutBtn.disabled = true;
        }

        // Calculate total manually since the endpoint might not provide it directly in the JSON sometimes
        const total = items.reduce((acc, item) => acc + (item.prodotto.prezzo * item.qtn), 0);
        if (totalEl) totalEl.innerText = `€ ${total.toFixed(2)}`;

        if (items.length === 0) {
            container.innerHTML = '<p class="text-center text-white mt-5">Il tuo carrello è vuoto.</p>';
            return;
        }

        container.innerHTML = items.map(item => `
            <div class="cart-item animate__animated animate__fadeIn">
                <div class="cart-item-img">
                    <i class="fas fa-tag"></i>
                </div>
                <div class="cart-item-info">
                    <div class="cart-item-name">${item.prodotto.nome}</div>
                    <div class="cart-item-price">${item.qtn} x € ${item.prodotto.prezzo.toFixed(2)}</div>
                </div>
                <button class="btn-remove-item" onclick="removeFromCart(${item.prodotto.id})">
                    <i class="fas fa-trash-alt"></i>
                </button>
            </div>
        `).join('');
    };

    /**
     * Auth Handlers
     */
    const initAuth = () => {
        const loginForm = document.getElementById('login-form') as HTMLFormElement | null;
        const registerForm = document.getElementById('register-form') as HTMLFormElement | null;

        loginForm?.addEventListener('submit', async (e) => {
            e.preventDefault();
            const username = (document.getElementById('login-username') as HTMLInputElement).value;
            const password = (document.getElementById('login-password') as HTMLInputElement).value;
            const loader = document.getElementById('login-loader');

            if (loader) loader.style.display = 'block';

            try {
                const response = await fetch('/api/auth/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ username, password })
                });

                if (response.ok) {
                    const token = await response.text();
                    localStorage.setItem('jwt_token', token);
                    showToast('Accesso effettuato!', 'success');
                    showView('shop');
                } else {
                    showToast('Credenziali non valide', 'error');
                }
            } catch (err) {
                showToast('Errore di connessione', 'error');
            } finally {
                if (loader) loader.style.display = 'none';
            }
        });

        registerForm?.addEventListener('submit', async (e) => {
            e.preventDefault();
            const username = (document.getElementById('reg-username') as HTMLInputElement).value;
            const mail = (document.getElementById('reg-email') as HTMLInputElement).value;
            const password = (document.getElementById('reg-password') as HTMLInputElement).value;
            const loader = document.getElementById('reg-loader');

            if (loader) loader.style.display = 'block';

            try {
                const response = await fetch('/api/auth/register', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ username, mail, password })
                });

                if (response.ok) {
                    showToast('Registrazione completata!', 'success');
                    window.toggleAuth('login');
                } else {
                    showToast('Errore registrazione', 'error');
                }
            } catch (err) {
                showToast('Errore di connessione', 'error');
            } finally {
                if (loader) loader.style.display = 'none';
            }
        });
    };

    /**
     * Utilities
     */
    const showToast = (message: string, type: ToastType = 'info') => {
        const container = document.getElementById('toast-container');
        if (!container) return;
        const toast = document.createElement('div');
        toast.className = `custom-toast ${type} animate__animated animate__fadeInRight`;
        toast.innerHTML = `<span>${message}</span>`;
        container.appendChild(toast);
        setTimeout(() => {
            toast.classList.replace('animate__fadeInRight', 'animate__fadeOutRight');
            setTimeout(() => toast.remove(), 500);
        }, 3000);
    };

    window.toggleAuth = (mode) => {
        const loginSection = document.getElementById('login-section');
        const registerSection = document.getElementById('register-section');
        if (!loginSection || !registerSection) return;
        if (mode === 'register') {
            loginSection.style.display = 'none';
            registerSection.style.display = 'block';
        } else {
            loginSection.style.display = 'block';
            registerSection.style.display = 'none';
        }
    };

    window.logout = () => {
        localStorage.removeItem('jwt_token');
        showView('auth');
    };

    window.addToCart = addToCart;
    window.removeFromCart = removeFromCart;
    window.clearCart = clearCart;

    // Startup
    document.addEventListener('DOMContentLoaded', () => {
        initAuth();
        if (localStorage.getItem('jwt_token')) {
            showView('shop');
        } else {
            showView('auth');
        }
    });

})();
export { };
