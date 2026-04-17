import { defineConfig } from 'vite';

export default defineConfig({
  server: {
    port: 5173,
    proxy: {
      // Proxy per evitare problemi di CORS durante lo sviluppo
      '/login': 'http://localhost:8080',
      '/logout': 'http://localhost:8080',
      '/public': 'http://localhost:8080',
      '/admin': 'http://localhost:8080',
      '/user': 'http://localhost:8080'
    }
  }
});
