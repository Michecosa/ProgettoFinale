"use strict";

/**
 * FinalShop - Main Application Logic
 */
(() => {
    // State
    let products = [];
    let currentCart = null;
    let searchTimeout = null;

    const getAuthHeaders = () => {
        const token = localStorage.getItem('jwt_token');
        return {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        };
    };

    /**
     * View Controllers
     */
    const showView = (view) => {
        const authView = document.getElementById('auth-view');
        const shopView = document.getElementById('shop-view');
        const mainNav = document.getElementById('main-nav');
        if (view === 'shop') {
            authView?.classList.add('d-none');
            shopView?.classList.remove('d-none');
            mainNav?.classList.remove('d-none');
            initShop();
        }
        else {
            authView?.classList.remove('d-none');
            shopView?.classList.add('d-none');
            mainNav?.classList.add('d-none');
        }
    };

    window.logout = () => {
        localStorage.removeItem('jwt_token');
        showToast('Sessione terminata', 'info');
        updateAuthUI();
        showHome();
    };

    /**
     * Product Logic
     */
    const fetchProducts = async (query = '') => {
        try {
            const response = await fetch('/api/products');
            if (response.ok) {
                products = await response.json();
                renderProducts(query);
            }
        } catch (err) {
            showToast('Errore caricamento prodotti', 'error');
        }
    };

    const renderProducts = (query = '') => {
        const grid = document.getElementById('products-grid');
        const countEl = document.getElementById('product-count');
        if (!grid) return;

        let filtered = products;
        if (query) {
            filtered = products.filter(p => 
                p.nome.toLowerCase().includes(query.toLowerCase())
            );
        }

        countEl.innerText = filtered.length;

        if (filtered.length === 0) {
            grid.innerHTML = '<div class="col-12 text-center py-5"><h3 class="text-muted">Nessun prodotto trovato</h3></div>';
            return;
        }

        grid.innerHTML = filtered.map(p => `
            <div class="col-md-4 col-lg-3 fade-in-up">
                <div class="product-card">
                    <div class="product-image-box">
                        <span class="badge-new">NEW</span>
                        <i class="fas fa-microchip"></i>
                        <div class="price-box">€ ${p.prezzo.toFixed(2)}</div>
                    </div>
                    <div class="product-info">
                        <div class="product-cat">Tecnologia</div>
                        <h5 class="product-name" title="${p.nome}">${p.nome}</h5>
                        <button class="btn-add" onclick="handleAddToCart(${p.id})">
                            <i class="fas fa-cart-plus me-2"></i> Aggiungi al carrello
                        </button>
                    </div>
                </div>
            </div>
        `).join('');
    };

    /**
     * Cart Logic
     */
    window.handleAddToCart = (productId) => {
        const token = localStorage.getItem('jwt_token');
        if (!token) {
            showToast('Devi accedere per acquistare', 'info');
            showAuth('login');
            return;
        }
        addToCart(productId);
    };

    const fetchCart = async () => {
        if (!localStorage.getItem('jwt_token')) return;
        try {
            const response = await fetch('/api/carts/mine', { headers: getAuthHeaders() });
            if (response.ok) {
                currentCart = await response.json();
                renderCart();
            }
        } catch (err) {
            console.error('Cart fetch failed', err);
        }
    };

    const addToCart = async (productId) => {
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
        }
        catch (err) {
            showToast('Errore nell\'aggiunta al carrello', 'error');
        }
    };

    window.removeFromCart = async (productId) => {
        try {
            const response = await fetch(`/api/carts/remove/${productId}`, {
                method: 'DELETE',
                headers: getAuthHeaders()
            });
            if (response.ok) {
                currentCart = await response.json();
                renderCart();
                showToast('Rimosso dal carrello', 'info');
            }
        } catch (err) {
            showToast('Errore rimozione', 'error');
        }
    };

    window.clearCart = async () => {
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
        const subtotalEl = document.getElementById('cart-subtotal');
        const checkoutBtn = document.getElementById('checkout-btn');

        if (!container || !currentCart) return;

        const items = currentCart.items || [];
        
        // Badge
        if (items.length > 0) {
            badge.classList.remove('d-none');
            const qtyCount = items.reduce((acc, i) => acc + i.qtn, 0);
            badge.innerText = qtyCount;
            checkoutBtn.disabled = false;
        } else {
            badge.classList.add('d-none');
            checkoutBtn.disabled = true;
        }

        // Totals
        const total = items.reduce((acc, i) => acc + (i.prodotto.prezzo * i.qtn), 0);
        const formattedTotal = `€ ${total.toFixed(2)}`;
        if (totalEl) totalEl.innerText = formattedTotal;
        if (subtotalEl) subtotalEl.innerText = formattedTotal;
        // Fix for specific ID in HTML
        const totalAlt = document.getElementById('cart-total text-gradient');
        if (totalAlt) totalAlt.innerText = formattedTotal;

        if (items.length === 0) {
            container.innerHTML = `
                <div class="text-center py-5">
                    <i class="fas fa-shopping-cart fa-3x mb-3 text-muted"></i>
                    <p class="text-muted">Il carrello è vuoto</p>
                </div>`;
            return;
        }

        container.innerHTML = items.map(item => `
            <div class="cart-item animate__animated animate__fadeIn">
                <div class="bg-dark rounded py-2 px-3">
                    <i class="fas fa-tag text-primary"></i>
                </div>
                <div class="cart-item-info">
                    <div class="fw-bold">${item.prodotto.nome}</div>
                    <div class="text-muted small">${item.qtn} x € ${item.prodotto.prezzo.toFixed(2)}</div>
                </div>
                <button class="btn btn-sm btn-outline-danger border-0" onclick="removeFromCart(${item.prodotto.id})">
                    <i class="fas fa-trash-alt"></i>
                </button>
            </div>
        `).join('');
    };

    /**
     * Utilities
     */
    const showToast = (message, type = 'success') => {
        const container = document.getElementById('toast-container');
        const toast = document.createElement('div');
        toast.className = `custom-toast toast-${type} animate__animated animate__fadeInRight`;
        
        let icon = 'fa-check-circle';
        if (type === 'error') icon = 'fa-exclamation-triangle';
        if (type === 'info') icon = 'fa-info-circle';

        toast.innerHTML = `
            <i class="fas ${icon} me-2 text-${type === 'success' ? 'success' : type === 'error' ? 'danger' : 'info'}"></i>
            <span class="text-white">${message}</span>
        `;
        
        container.appendChild(toast);
        setTimeout(() => {
            toast.classList.replace('animate__fadeInRight', 'animate__fadeOutRight');
            setTimeout(() => toast.remove(), 500);
        }, 3000);
    };

    /**
     * Initialization
     */
    const initApp = () => {
        // Form Handlers
        const loginForm = document.getElementById('login-form');
        const registerForm = document.getElementById('register-form');

        loginForm?.addEventListener('submit', async (e) => {
            e.preventDefault();
            const username = document.getElementById('login-username').value;
            const password = document.getElementById('login-password').value;
            const text = document.getElementById('login-text');
            const spinner = document.getElementById('login-spinner');

            text.classList.add('d-none');
            spinner.classList.remove('d-none');

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
                }
                else {
                    showToast('Credenziali non valide', 'error');
                }
            }
            catch (err) {
                showToast('Errore di connessione', 'error');
            }
            finally {
                if (loader)
                    loader.style.display = 'none';
            }
        });

        registerForm?.addEventListener('submit', async (e) => {
            e.preventDefault();
            const username = document.getElementById('reg-username').value;
            const mail = document.getElementById('reg-email').value;
            const password = document.getElementById('reg-password').value;
            const text = document.getElementById('reg-text');
            const spinner = document.getElementById('reg-spinner');

            text.classList.add('d-none');
            spinner.classList.remove('d-none');

            try {
                const response = await fetch('/api/auth/register', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ username, mail, password })
                });

                if (response.ok) {
                    showToast('Registrazione completata!', 'success');
                    window.toggleAuth('login');
                }
                else {
                    showToast('Errore registrazione', 'error');
                }
            }
            catch (err) {
                showToast('Errore di connessione', 'error');
            }
            finally {
                if (loader)
                    loader.style.display = 'none';
            }
        });
    };
    /**
     * Utilities
     */
    const showToast = (message, type = 'info') => {
        const container = document.getElementById('toast-container');
        if (!container)
            return;
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
        if (!loginSection || !registerSection)
            return;
        if (mode === 'register') {
            loginSection.style.display = 'none';
            registerSection.style.display = 'block';
        }
        else {
            loginSection.style.display = 'block';
            registerSection.style.display = 'none';
        }
    };
    window.logout = () => {
        localStorage.removeItem('jwt_token');
        showView('auth');
    };

    document.addEventListener('DOMContentLoaded', initApp);
})();
