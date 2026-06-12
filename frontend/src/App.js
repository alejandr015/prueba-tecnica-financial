import React, { useState } from 'react';
import Clientes from './components/Clientes';
import Productos from './components/Productos';
import Transacciones from './components/Transacciones';
import './index.css';

function App() {
  const [tab, setTab] = useState('clientes');

  return (
    <div>
      <div className="navbar">🏦 Financial App</div>
      <div className="container">
        <div className="tabs">
          <button className={`tab ${tab === 'clientes' ? 'active' : ''}`} onClick={() => setTab('clientes')}>Clientes</button>
          <button className={`tab ${tab === 'productos' ? 'active' : ''}`} onClick={() => setTab('productos')}>Productos</button>
          <button className={`tab ${tab === 'transacciones' ? 'active' : ''}`} onClick={() => setTab('transacciones')}>Transacciones</button>
        </div>
        {tab === 'clientes' && <Clientes />}
        {tab === 'productos' && <Productos />}
        {tab === 'transacciones' && <Transacciones />}
      </div>
    </div>
  );
}

export default App;