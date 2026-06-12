import React, { useState, useEffect } from 'react';
import api from '../api';

export default function Productos() {
  const [productos, setProductos] = useState([]);
  const [clientes, setClientes] = useState([]);
  const [form, setForm] = useState({ tipoCuenta: 'AHORROS', saldo: '', exentaGMF: false, clienteId: '' });
  const [msg, setMsg] = useState(null);

  useEffect(() => {
    cargar();
    api.get('/clientes').then(r => setClientes(r.data));
  }, []);

  const cargar = async () => {
    const res = await api.get('/clientes');
    const all = [];
    for (const c of res.data) {
      const prod = await api.get(`/productos/cliente/${c.id}`);
      all.push(...prod.data);
    }
    setProductos(all);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/productos', { ...form, saldo: parseFloat(form.saldo) });
      setMsg({ type: 'success', text: 'Producto creado correctamente' });
      setForm({ tipoCuenta: 'AHORROS', saldo: '', exentaGMF: false, clienteId: '' });
      cargar();
    } catch (err) {
      setMsg({ type: 'error', text: err.response?.data?.error || 'Error al crear' });
    }
  };

  const cambiarEstado = async (id, estado) => {
    try {
      await api.patch(`/productos/${id}/estado?estado=${estado}`);
      setMsg({ type: 'success', text: `Cuenta ${estado.toLowerCase()}` });
      cargar();
    } catch (err) {
      setMsg({ type: 'error', text: err.response?.data?.error || 'Error' });
    }
  };

  const getBadge = (estado) => {
    if (estado === 'ACTIVA') return 'badge badge-active';
    if (estado === 'INACTIVA') return 'badge badge-inactive';
    return 'badge badge-cancelled';
  };

  return (
    <div>
      <div className="card">
        <h2>Nueva Cuenta</h2>
        {msg && <div className={`alert alert-${msg.type}`}>{msg.text}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-grid">
            <div className="form-group">
              <label>Tipo de Cuenta</label>
              <select value={form.tipoCuenta} onChange={e => setForm({...form, tipoCuenta: e.target.value})}>
                <option value="AHORROS">Ahorros</option>
                <option value="CORRIENTE">Corriente</option>
              </select>
            </div>
            <div className="form-group">
              <label>Saldo Inicial</label>
              <input type="number" value={form.saldo} onChange={e => setForm({...form, saldo: e.target.value})} required />
            </div>
            <div className="form-group">
              <label>Cliente</label>
              <select value={form.clienteId} onChange={e => setForm({...form, clienteId: e.target.value})} required>
                <option value="">Seleccione...</option>
                {clientes.map(c => <option key={c.id} value={c.id}>{c.nombres} {c.apellido}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label>Exenta GMF</label>
              <select value={form.exentaGMF} onChange={e => setForm({...form, exentaGMF: e.target.value === 'true'})}>
                <option value="false">No</option>
                <option value="true">Sí</option>
              </select>
            </div>
          </div>
          <button type="submit" className="btn btn-primary">Crear Cuenta</button>
        </form>
      </div>

      <div className="card">
        <h2>Cuentas Registradas</h2>
        <table>
          <thead>
            <tr>
              <th>ID</th><th>Tipo</th><th>Número</th><th>Estado</th><th>Saldo</th><th>Cliente</th><th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {productos.map(p => (
              <tr key={p.id}>
                <td>{p.id}</td>
                <td>{p.tipoCuenta}</td>
                <td>{p.numeroCuenta}</td>
                <td><span className={getBadge(p.estado)}>{p.estado}</span></td>
                <td>${p.saldo?.toLocaleString()}</td>
                <td>{p.cliente?.nombres} {p.cliente?.apellido}</td>
                <td className="actions">
                  {p.estado === 'ACTIVA' && <button className="btn btn-warning btn-sm" onClick={() => cambiarEstado(p.id, 'INACTIVA')}>Inactivar</button>}
                  {p.estado === 'INACTIVA' && <button className="btn btn-success btn-sm" onClick={() => cambiarEstado(p.id, 'ACTIVA')}>Activar</button>}
                  {p.estado !== 'CANCELADA' && <button className="btn btn-danger btn-sm" onClick={() => cambiarEstado(p.id, 'CANCELADA')}>Cancelar</button>}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}