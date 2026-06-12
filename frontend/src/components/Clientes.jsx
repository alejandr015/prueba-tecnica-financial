import React, { useState, useEffect } from 'react';
import api from '../api';

export default function Clientes() {
  const [clientes, setClientes] = useState([]);
  const [form, setForm] = useState({ tipoIdentificacion: 'CC', numeroIdentificacion: '', nombres: '', apellido: '', correoElectronico: '', fechaNacimiento: '' });
  const [editId, setEditId] = useState(null);
  const [msg, setMsg] = useState(null);

  useEffect(() => { cargar(); }, []);

  const cargar = async () => {
    const res = await api.get('/clientes');
    setClientes(res.data);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editId) {
        await api.put(`/clientes/${editId}`, form);
        setMsg({ type: 'success', text: 'Cliente actualizado correctamente' });
      } else {
        await api.post('/clientes', form);
        setMsg({ type: 'success', text: 'Cliente creado correctamente' });
      }
      setForm({ tipoIdentificacion: 'CC', numeroIdentificacion: '', nombres: '', apellido: '', correoElectronico: '', fechaNacimiento: '' });
      setEditId(null);
      cargar();
    } catch (err) {
      setMsg({ type: 'error', text: err.response?.data?.error || 'Error al guardar' });
    }
  };

  const handleEdit = (c) => {
    setEditId(c.id);
    setForm({ tipoIdentificacion: c.tipoIdentificacion, numeroIdentificacion: c.numeroIdentificacion, nombres: c.nombres, apellido: c.apellido, correoElectronico: c.correoElectronico, fechaNacimiento: c.fechaNacimiento });
  };

  const handleDelete = async (id) => {
    if (!window.confirm('¿Eliminar cliente?')) return;
    try {
      await api.delete(`/clientes/${id}`);
      setMsg({ type: 'success', text: 'Cliente eliminado' });
      cargar();
    } catch (err) {
      setMsg({ type: 'error', text: err.response?.data?.error || 'Error al eliminar' });
    }
  };

  return (
    <div>
      <div className="card">
        <h2>{editId ? 'Editar Cliente' : 'Nuevo Cliente'}</h2>
        {msg && <div className={`alert alert-${msg.type}`}>{msg.text}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-grid">
            <div className="form-group">
              <label>Tipo Identificación</label>
              <select value={form.tipoIdentificacion} onChange={e => setForm({...form, tipoIdentificacion: e.target.value})}>
                <option value="CC">CC</option>
                <option value="CE">CE</option>
                <option value="NIT">NIT</option>
                <option value="PP">Pasaporte</option>
              </select>
            </div>
            <div className="form-group">
              <label>Número Identificación</label>
              <input value={form.numeroIdentificacion} onChange={e => setForm({...form, numeroIdentificacion: e.target.value})} required />
            </div>
            <div className="form-group">
              <label>Nombres</label>
              <input value={form.nombres} onChange={e => setForm({...form, nombres: e.target.value})} required />
            </div>
            <div className="form-group">
              <label>Apellido</label>
              <input value={form.apellido} onChange={e => setForm({...form, apellido: e.target.value})} required />
            </div>
            <div className="form-group">
              <label>Correo Electrónico</label>
              <input type="email" value={form.correoElectronico} onChange={e => setForm({...form, correoElectronico: e.target.value})} required />
            </div>
            <div className="form-group">
              <label>Fecha Nacimiento</label>
              <input type="date" value={form.fechaNacimiento} onChange={e => setForm({...form, fechaNacimiento: e.target.value})} required />
            </div>
          </div>
          <div className="actions">
            <button type="submit" className="btn btn-primary">{editId ? 'Actualizar' : 'Crear'}</button>
            {editId && <button type="button" className="btn btn-warning" onClick={() => { setEditId(null); setForm({ tipoIdentificacion: 'CC', numeroIdentificacion: '', nombres: '', apellido: '', correoElectronico: '', fechaNacimiento: '' }); }}>Cancelar</button>}
          </div>
        </form>
      </div>

      <div className="card">
        <h2>Clientes Registrados</h2>
        <table>
          <thead>
            <tr>
              <th>ID</th><th>Tipo</th><th>Identificación</th><th>Nombre</th><th>Correo</th><th>Nacimiento</th><th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {clientes.map(c => (
              <tr key={c.id}>
                <td>{c.id}</td>
                <td>{c.tipoIdentificacion}</td>
                <td>{c.numeroIdentificacion}</td>
                <td>{c.nombres} {c.apellido}</td>
                <td>{c.correoElectronico}</td>
                <td>{c.fechaNacimiento}</td>
                <td className="actions">
                  <button className="btn btn-warning btn-sm" onClick={() => handleEdit(c)}>Editar</button>
                  <button className="btn btn-danger btn-sm" onClick={() => handleDelete(c.id)}>Eliminar</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}