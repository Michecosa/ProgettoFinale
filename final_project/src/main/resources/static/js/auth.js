"use strict";

/**
 * FinalShop - Main Application Logic
 */
(() => {
    // State
    let products = [];
    let orders = [];
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
    const showView = (viewName) => {
        const views = ['home-view', 'shop-view', 'auth-view', 'orders-view', 'profile-view'];
        views.forEach(v => {
            const el = document.getElementById(v);
            if (el) el.classList.add('d-none');
        });

        const activeView = document.getElementById(`${viewName}-view`);
        if (activeView) activeView.classList.remove('d-none');
        
        // Update background if needed
        if (viewName === 'auth') {
            document.body.style.background = 'var(--bg-main)';
        } else {
            document.body.style.background = 'var(--bg-main)';
        }

        // Scroll to top
        window.scrollTo(0, 0);

        if (viewName === 'shop') {
            fetchProducts();
        }
    };

    window.showHome = () => {
        showView('home');
        fetchProducts();
    };
    window.showShop = () => showView('shop');
    window.showOrders = () => {
        const token = localStorage.getItem('jwt_token');
        if (!token) {
            showToast('Devi accedere per vedere i tuoi ordini', 'info');
            showAuth('login');
            return;
        }
        showView('orders');
        fetchOrders();
    };
    window.showAuth = (mode) => {
        showView('auth');
        window.toggleAuth(mode);
    };

    /**
     * Profile Logic
     */
    window.showProfile = () => {
        const token = localStorage.getItem('jwt_token');
        if (!token) {
            showToast('Devi accedere per vedere il profilo', 'info');
            showAuth('login');
            return;
        }
        showView('profile');
        fetchProfile();
    };

    const fetchProfile = async () => {
        try {
            const response = await fetch('/api/auth/me', { headers: getAuthHeaders() });
            if (response.ok) {
                const user = await response.json();
                renderProfile(user);
            } else if (response.status === 401 || response.status === 403) {
                localStorage.removeItem('jwt_token');
                updateAuthUI();
                showToast('Sessione scaduta. Accedi di nuovo.', 'error');
                showAuth('login');
            }
        } catch (err) {
            // Fallback: parse username from JWT payload
            try {
                const token = localStorage.getItem('jwt_token');
                const payload = JSON.parse(atob(token.split('.')[1]));
                renderProfile({ username: payload.sub || payload.username || 'Utente', mail: '' });
            } catch {
                showToast('Impossibile caricare il profilo', 'error');
            }
        }
    };

    const renderProfile = async (user) => {
        document.getElementById('profile-display-name').textContent = user.username || '\u2014';
        document.getElementById('profile-display-email').textContent = user.mail || user.email || '\u2014';
        document.getElementById('profile-username').textContent = user.username || '\u2014';
        document.getElementById('profile-email').textContent = user.mail || user.email || '\u2014';

        try {
            const token = localStorage.getItem('jwt_token');
            const payload = JSON.parse(atob(token.split('.')[1]));
            const roles = (payload.roles || '').split(',').map(r => r.trim()).filter(Boolean);
            const badgesEl = document.getElementById('profile-role-badges');
            badgesEl.innerHTML = roles.map(role => {
                if (role === 'ROLE_ADMIN') {
                    return '<span class="badge bg-warning text-white me-1"><i class="fas fa-crown me-1"></i>Utente Premium</span>';
                } else if (role === 'ROLE_USER') {
                    return '<span class="badge bg-secondary me-1"><i class="fas fa-user me-1"></i>Utente Normale</span>';
                }
                return '';
            }).join('');
        } catch {
            document.getElementById('profile-role-badges').innerHTML = '';
        }

        document.getElementById('profile-last-login').textContent =
            new Date().toLocaleDateString('it-IT');

        try {
            const resp = await fetch('/api/orders', { headers: getAuthHeaders() });
            if (resp.ok) {
                const orderList = await resp.json();
                document.getElementById('profile-orders-count').textContent = orderList.length;
                const total = orderList.reduce((acc, o) =>
                    acc + o.items.reduce((s, i) => s + (i.prodotto.prezzo * i.qtn), 0), 0);
                document.getElementById('profile-spent-total').textContent = '\u20ac ' + total.toFixed(2);
            }
        } catch {
            document.getElementById('profile-orders-count').textContent = '\u2014';
            document.getElementById('profile-spent-total').textContent = '\u2014';
        }
    };

    window.handleChangePassword = async () => {
        const currentPw = document.getElementById('current-password').value;
        const newPw = document.getElementById('new-password').value;
        const confirmPw = document.getElementById('confirm-password').value;

        if (!currentPw || !newPw || !confirmPw) {
            showToast('Compila tutti i campi', 'error');
            return;
        }
        if (newPw.length < 6) {
            showToast('La nuova password deve avere almeno 6 caratteri', 'error');
            return;
        }
        if (newPw !== confirmPw) {
            showToast('Le password non coincidono', 'error');
            return;
        }

        const text = document.getElementById('change-pw-text');
        const spinner = document.getElementById('change-pw-spinner');
        text.classList.add('d-none');
        spinner.classList.remove('d-none');

        try {
            const response = await fetch('/api/auth/change-password', {
                method: 'PUT',
                headers: getAuthHeaders(),
                body: JSON.stringify({ currentPassword: currentPw, newPassword: newPw })
            });
            if (response.ok) {
                showToast('Password aggiornata con successo!', 'success');
                document.getElementById('current-password').value = '';
                document.getElementById('new-password').value = '';
                document.getElementById('confirm-password').value = '';
            } else {
                const msg = await response.text();
                showToast(msg || 'Errore durante il cambio password', 'error');
            }
        } catch {
            showToast('Errore di connessione', 'error');
        } finally {
            text.classList.remove('d-none');
            spinner.classList.add('d-none');
        }
    };


    /**
     * Auth Logic
     */
    window.toggleAuth = (mode) => {
        const login = document.getElementById('login-section');
        const register = document.getElementById('register-section');
        if (mode === 'register') {
            login.classList.add('d-none');
            register.classList.remove('d-none');
        } else {
            login.classList.remove('d-none');
            register.classList.add('d-none');
        }
    };

    const updateAuthUI = () => {
        const token = localStorage.getItem('jwt_token');
        const userActions = document.getElementById('user-actions');
        const profileDropdown = document.getElementById('profile-dropdown');
        
        if (token) {
            userActions.classList.add('d-none');
            profileDropdown.classList.remove('d-none');
            fetchCart();
        } else {
            userActions.classList.remove('d-none');
            profileDropdown.classList.add('d-none');
            document.getElementById('cart-badge').classList.add('d-none');
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
                renderFeaturedProducts();
            }
        } catch (err) {
            showToast('Errore caricamento prodotti', 'error');
        }
    };

    const techIcon = (nome) => {
        const n = (nome || '').toLowerCase();
        if (n.includes('node.js') || n.includes('nodejs')) return { cls: 'fab', icon: 'fa-node-js' };
        if (n.includes('react')) return { cls: 'fab', icon: 'fa-react' };
        if (n.includes('vue')) return { cls: 'fab', icon: 'fa-vuejs' };
        if (n.includes('angular')) return { cls: 'fab', icon: 'fa-angular' };
        if (n.includes('python')) return { cls: 'fab', icon: 'fa-python' };
        if (n.includes('java') && !n.includes('javascript')) return { cls: 'fab', icon: 'fa-java' };
        if (n.includes('javascript') || n.includes('js')) return { cls: 'fab', icon: 'fa-js' };
        if (n.includes('php')) return { cls: 'fab', icon: 'fa-php' };
        if (n.includes('docker')) return { cls: 'fab', icon: 'fa-docker' };
        if (n.includes('aws') || n.includes('amazon')) return { cls: 'fab', icon: 'fa-aws' };
        if (n.includes('html')) return { cls: 'fab', icon: 'fa-html5' };
        if (n.includes('css')) return { cls: 'fab', icon: 'fa-css3-alt' };
        if (n.includes('github') || n.includes('git')) return { cls: 'fab', icon: 'fa-git-alt' };
        if (n.includes('linux')) return { cls: 'fab', icon: 'fa-linux' };
        return null;
    };

    const categoryIcon = (categorie) => {
        if (!categorie || categorie.length === 0) return { cls: 'fas', icon: 'fa-code' };
        const nome = categorie[0].nome.toLowerCase();
        if (nome.includes('corso') || nome.includes('corsi')) return { cls: 'fas', icon: 'fa-graduation-cap' };
        if (nome.includes('framework') || nome.includes('libreri')) return { cls: 'fas', icon: 'fa-cubes' };
        if (nome.includes('tool') || nome.includes('utility') || nome.includes('strument')) return { cls: 'fas', icon: 'fa-tools' };
        if (nome.includes('template') || nome.includes('starter')) return { cls: 'fas', icon: 'fa-file-code' };
        if (nome.includes('plugin') || nome.includes('estension')) return { cls: 'fas', icon: 'fa-puzzle-piece' };
        if (nome.includes('ebook') || nome.includes('guida') || nome.includes('libro')) return { cls: 'fas', icon: 'fa-book-open' };
        return { cls: 'fas', icon: 'fa-code' };
    };

    const getProductIcon = (p) => techIcon(p.nome) || categoryIcon(p.categorie);

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

        grid.innerHTML = filtered.map(p => {
            const { cls, icon } = getProductIcon(p);
            return `
            <div class="col-md-4 col-lg-3 fade-in-up">
                <div class="product-card">
                    <div class="product-image-box">
                        <span class="badge-new">NEW</span>
                        <i class="${cls} ${icon}"></i>
                        <div class="price-box">€ ${p.prezzo.toFixed(2)}</div>
                    </div>
                    <div class="product-info">
                        <div class="product-cat">${p.categorie && p.categorie.length > 0 ? p.categorie.map(c => c.nome).join(', ') : 'Senza categoria'}</div>
                        <h5 class="product-name" title="${p.nome}">${p.nome}</h5>
                        <button class="btn-add" onclick="handleAddToCart(${p.id})">
                            <i class="fas fa-cart-plus me-2"></i> Aggiungi al carrello
                        </button>
                    </div>
                </div>
            </div>
        `;
        }).join('');
    };

    const renderFeaturedProducts = () => {
        const grid = document.getElementById('featured-products');
        if (!grid) return;

        // Take only first 4 products for featured section
        const featured = products.slice(0, 4);

        if (featured.length === 0) {
            grid.innerHTML = '<div class="col-12 text-center py-5"><p class="text-muted">Nessun prodotto disponibile al momento</p></div>';
            return;
        }

        grid.innerHTML = featured.map(p => {
            const { cls, icon } = getProductIcon(p);
            return `
            <div class="col-md-6 col-lg-3 fade-in-up">
                <div class="product-card">
                    <div class="product-image-box">
                        <span class="badge-new">NEW</span>
                        <i class="${cls} ${icon}"></i>
                        <div class="price-box">€ ${p.prezzo.toFixed(2)}</div>
                    </div>
                    <div class="product-info">
                        <div class="product-cat">${p.categorie && p.categorie.length > 0 ? p.categorie[0].nome : 'Senza categoria'}</div>
                        <h5 class="product-name">${p.nome}</h5>
                        <button class="btn btn-outline-primary w-100 rounded-pill mt-2" onclick="handleAddToCart(${p.id})">
                            <i class="fas fa-cart-plus me-2"></i> Aggiungi
                        </button>
                    </div>
                </div>
            </div>
        `;
        }).join('');
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

    const addToCart = async (productId, qtn = 1) => {
        try {
            const response = await fetch(`/api/carts/add/${productId}?qtn=${qtn}`, {
                method: 'POST',
                headers: getAuthHeaders()
            });
            if (response.ok) {
                currentCart = await response.json();
                renderCart();
                if (qtn > 0) showToast('Carrello aggiornato!', 'success');
            }
        } catch (err) {
            showToast('Errore aggiornamento carrello', 'error');
        }
    };

    window.updateItemQuantity = async (productId, delta) => {
        const item = currentCart.items.find(i => i.prodotto.id === productId);
        if (!item) return;

        if (item.qtn + delta <= 0) {
            removeFromCart(productId);
            return;
        }

        // We use a modified version of add that supports subtraction if the service allows it
        // BUT wait, the current service doesn't allow qtn <= 0.
        // Let's modify the service to be more robust or handle it here by setting absolute quantity.
        // Actually, I'll modify the SERVICE to allow delta addition.
        addToCart(productId, delta);
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

        if (items.length === 0) {
            container.innerHTML = `
                <div class="text-center py-5">
                    <i class="fas fa-shopping-cart fa-3x mb-3 text-white"></i>
                    <p class="text-white">Il carrello è vuoto</p>
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
                    <div class="text-muted small">€ ${item.prodotto.prezzo.toFixed(2)} cad.</div>
                    <div class="d-flex align-items-center mt-2">
                        <div class="btn-group btn-group-sm">
                            <button class="btn btn-outline-secondary btn-sm px-2 py-0 border-secondary" onclick="updateItemQuantity(${item.prodotto.id}, -1)">-</button>
                            <span class="px-3 border-top border-bottom border-secondary bg-dark" style="line-height: 1.8;">${item.qtn}</span>
                            <button class="btn btn-outline-secondary btn-sm px-2 py-0 border-secondary" onclick="updateItemQuantity(${item.prodotto.id}, 1)">+</button>
                        </div>
                    </div>
                </div>
                <button class="btn btn-sm btn-outline-danger border-0" onclick="removeFromCart(${item.prodotto.id})">
                    <i class="fas fa-trash-alt"></i>
                </button>
            </div>
        `).join('');
    };

    /**
     * Orders Logic
     */
    const fetchOrders = async () => {
        try {
            const response = await fetch('/api/orders', { headers: getAuthHeaders() });
            if (response.ok) {
                orders = await response.json();
                renderOrders();
            }
        } catch (err) {
            showToast('Errore caricamento ordini', 'error');
        }
    };

    const renderOrders = () => {
        const container = document.getElementById('orders-container');
        if (!container) return;

        if (orders.length === 0) {
            container.innerHTML = `
                <div class="col-12 text-center py-5">
                    <i class="fas fa-box-open fa-3x mb-3 text-white"></i>
                    <h3 class="text-white mb-5">Non hai ancora effettuato ordini</h3>
                    <button class="btn btn-primary-custom mt-3" onclick="showShop()">Inizia a fare acquisti</button>
                </div>`;
            return;
        }

        container.innerHTML = orders.map(order => `
            <div class="col-12 fade-in-up">
                <div class="order-card">
                    <div class="order-card-header">
                        <div>
                            <span class="order-label">ORDINE EFFETTUATO</span>
                            <span class="order-value">${new Date(order.data).toLocaleDateString()}</span>
                        </div>
                        <div>
                            <span class="order-label">TOTALE</span>
                            <span class="order-value text-gradient">€ ${order.items.reduce((acc, i) => acc + (i.prodotto.prezzo * i.qtn), 0).toFixed(2)}</span>
                        </div>
                        <div>
                            <span class="order-label">ORDINE #</span>
                            <span class="order-value">${order.id}</span>
                        </div>
                        <span class="badge rounded-pill bg-success px-3 py-2">Consegnato</span>
                    </div>
                    <div class="order-card-body">
                        <div class="row">
                            <div class="col-md-8">
                                ${order.items.map(item => `
                                    <div class="d-flex align-items-center mb-3">
                                        <div class="order-item-icon me-3">
                                            <i class="fas fa-tag"></i>
                                        </div>
                                        <div>
                                            <h6 class="mb-0 fw-bold text-white">${item.prodotto.nome}</h6>
                                            <p class="order-item-meta mb-0">Quantità: ${item.qtn} • Prezzo unitario: € ${item.prodotto.prezzo.toFixed(2)}</p>
                                        </div>
                                    </div>
                                `).join('')}
                            </div>
                            <div class="col-md-4 order-delivery-col py-2">
                                <h6 class="fw-bold mb-3 text-white">Dettagli Consegna</h6>
                                <p class="order-item-meta mb-1"><i class="fas fa-map-marker-alt me-2 text-primary"></i> ${order.indirizzo}</p>
                                <p class="order-item-meta"><i class="fas fa-truck me-2 text-primary"></i> Consegna stimata: ${new Date(order.consegna).toLocaleDateString()}</p>
                                <button class="btn-track-order w-100 mt-2">Traccia pacco</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `).join('');
    };

    window.handleCheckout = async () => {
        const token = localStorage.getItem('jwt_token');
        if (!token) {
            showToast('Accedi per completare l\'acquisto', 'info');
            showAuth('login');
            return;
        }

        if (!currentCart || !currentCart.items || currentCart.items.length === 0) {
            showToast('Il tuo carrello è vuoto', 'error');
            return;
        }

        const indirizzo = prompt("Inserisci l'indirizzo di spedizione:");
        if (!indirizzo) return;

        try {
            const response = await fetch(`/api/orders?indirizzo=${encodeURIComponent(indirizzo)}`, {
                method: 'POST',
                headers: getAuthHeaders()
            });

            if (response.ok) {
                showToast('Ordine creato con successo!', 'success');

                // Close offcanvas if open
                const cartEl = document.getElementById('cartOffcanvas');
                const offcanvas = bootstrap.Offcanvas.getInstance(cartEl) || new bootstrap.Offcanvas(cartEl);
                offcanvas.hide();

                // Refresh state
                await fetchCart();
                showOrders();
            } else if (response.status === 401 || response.status === 403) {
                localStorage.removeItem('jwt_token');
                updateAuthUI();
                showToast('Sessione scaduta. Effettua di nuovo l\'accesso.', 'error');
                showAuth('login');
            } else {
                const errMsg = await response.text();
                showToast(`Errore: ${errMsg || 'Impossibile completare l\'ordine'}`, 'error');
            }
        } catch (err) {
            showToast('Errore di connessione durante il checkout', 'error');
        }
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
                    updateAuthUI();
                    showHome();
                } else {
                    showToast('Credenziali non valide', 'error');
                }
            } catch (err) {
                showToast('Errore di connessione', 'error');
            } finally {
                text.classList.remove('d-none');
                spinner.classList.add('d-none');
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
                    showToast('Account creato con successo!', 'success');
                    window.toggleAuth('login');
                } else {
                    const err = await response.text();
                    showToast(err || 'Errore registrazione', 'error');
                }
            } catch (err) {
                showToast('Errore di connessione', 'error');
            } finally {
                text.classList.remove('d-none');
                spinner.classList.add('d-none');
            }
        });

        // Search Handler
        document.getElementById('search-input')?.addEventListener('input', (e) => {
            clearTimeout(searchTimeout);
            searchTimeout = setTimeout(() => {
                renderProducts(e.target.value);
            }, 300);
        });

        // Refresh cart when offcanvas shown
        const cartOffcanvas = document.getElementById('cartOffcanvas');
        cartOffcanvas?.addEventListener('show.bs.offcanvas', () => {
            fetchCart();
        });

        // Init UI
        updateAuthUI();
        showHome();
    };

    document.addEventListener('DOMContentLoaded', initApp);
})();