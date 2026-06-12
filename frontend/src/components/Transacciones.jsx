import React, { useState, useEffect } from 'react';
import api from '../api';

export default function Transacciones() {
  const [productos, setProductos] = useState([]);
  const [movimientos, setMovimientos] = useState([]);
  const [form, setForm] = useState({ tipo: 'CONSIGNACION', monto: '', productoId: '', cuentaDestinoId: '', descripcion: '' });
  const [msg, setMsg] = useState(null);
  const [cuentaVer, setCuentaVer] = useState('');

  useEffect(() => { cargarProductos(); }, []);

  const cargarProductos = async () => {
    const clientes = await api.get('/clientes');
    const all = [];
    for (const c of clientes.data) {
      const prod = await api.get(`/productos/cliente/${c.id}`);
      all.push(...prod.data);
    }
    setProductos(all);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/transacciones', {
        ...form,
        monto: parseFloat(form.monto),
        productoId: parseInt(form.productoId),
        cuentaDestinoId: form.cuentaDestinoId ? parseInt(form.cuentaDestinoId) : null
      });
      setMsg({ type: 'success', text: 'Transacción realizada correctamente' });
      setForm({ tipo: 'CONSIGNACION', monto: '', productoId: '', cuentaDestinoId: '', descripcion: '' });
      cargarProductos();
    } catch (err) {
      setMsg({ type: 'error', text: err.response?.data?.error || 'Error' });
    }
  };

  const verMovimientos = async () => {
    if (!cuentaVer) return;
    const res = await api.get(`/transacciones/producto/${cuentaVer}`);
    setMovimientos(res.data);
  };

  return (
    <div>
      <div className="card">
        <h2>Nueva Transacción</h2>
        {msg && <div className={`alert alert-${msg.type}`}>{msg.text}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-grid">
            <div className="form-group">
              <label>Tipo</label>
              <select value={form.tipo} onChange={e => setForm({...form, tipo: e.target.value})}>
                <option value="CONSIGNACION">Consignación</option>
                <option value="RETIRO">Retiro</option>
                <option value="TRANSFERENCIA">Transferencia</option>
              </select>
            </div>
            <div className="form-group">
              <label>Monto</label>
              <input type="number" value={form.monto} onChange={e => setForm({...form, monto: e.target.value})} required />
            </div>
            <div className="form-group">
              <label>Cuenta Origen</label>
              <select value={form.productoId} onChange={e => setForm({...form, productoId: e.target.value})} required>
                <option value="">Seleccione...</option>
                {productos.map(p => <option key={p.id} value={p.id}>{p.numeroCuenta} - {p.tipoCuenta} (${p.saldo?.toLocaleString()})</option>)}
              </select>
            </div>
            {form.tipo === 'TRANSFERENCIA' && (
              <div className="form-group">
                <label>Cuenta Destino</label>
                <select value={form.cuentaDestinoId} onChange={e => setForm({...form, cuentaDestinoId: e.target.value})}>
                  <option value="">Seleccione...</option>
                  {productos.filter(p => p.id !== parseInt(form.productoId)).map(p => <option key={p.id} value={p.id}>{p.numeroCuenta} - {p.tipoCuenta}</option>)}
                </select>
              </div>
            )}
            <div className="form-group">
              <label>Descripción</label>
              <input value={form.descripcion} onChange={e => setForm({...form, descripcion: e.target.value})} />
            </div>
          </div>
          <button type="submit" className="btn btn-primary">Realizar Transacción</button>
        </form>
      </div>

      <div className="card">
        <h2>Estado de Cuenta</h2>
        <div className="form-grid" style={{marginBottom: 16}}>
          <div className="form-group">
            <label>Seleccionar Cuenta</label>
            <select value={cuentaVer} onChange={e => setCuentaVer(e.target.value)}>
              <option value="">Seleccione...</option>
              {productos.map(p => <option key={p.id} value={p.id}>{p.numeroCuenta} - {p.tipoCuenta}</option>)}
            </select>
          </div>
        </div>
        <button className="btn btn-primary" onClick={verMovimientos} style={{marginBottom: 16}}>Ver Movimientos</button>
        {movimientos.length > 0 && (
          <table>
            <thead>
              <tr><th>ID</th><th>Tipo</th><th>Monto</th><th>Fecha</th><th>Descripción</th></tr>
            </thead>
            <tbody>
              {movimientos.map(m => (
                <tr key={m.id}>
                  <td>{m.id}</td>
                  <td>{m.tipo}</td>
                  <td>${m.monto?.toLocaleString()}</td>
                  <td>{new Date(m.fecha).toLocaleString()}</td>
                  <td>{m.descripcion}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}