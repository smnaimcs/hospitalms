import React, { useState } from 'react';
import { nanoid } from 'nanoid';

function Users() {
  const roles = ['Admin', 'Doctor', 'Patient'];
  const [users, setUsers] = useState([
    { id: nanoid(), name: 'John Doe', role: 'Admin', active: true },
    { id: nanoid(), name: 'Dr. Smith', role: 'Doctor', active: true },
    { id: nanoid(), name: 'Jane Doe', role: 'Patient', active: false },
  ]);

  const [newUser, setNewUser] = useState({ name: '', role: roles[0], active: true });

  const handleAddUser = () => {
    if (!newUser.name) return;
    const user = { ...newUser, id: nanoid() };
    setUsers([...users, user]);
    setNewUser({ name: '', role: roles[0], active: true });
  };

  const toggleActive = (id) => {
    setUsers(users.map((u) => (u.id === id ? { ...u, active: !u.active } : u)));
  };

  return (
    <div className="space-y-4 p-4">
      <h1 className="text-xl font-semibold mb-4 text-white">User Management</h1>

      <div className="bg-surface p-4 rounded shadow grid md:grid-cols-3 gap-4 items-end">
        <input
          type="text"
          placeholder="Name"
          className="bg-gray-800 text-white border border-gray-700 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary"
          value={newUser.name}
          onChange={(e) => setNewUser({ ...newUser, name: e.target.value })}
        />
        <select
          className="bg-gray-800 text-white border border-gray-700 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary"
          value={newUser.role}
          onChange={(e) => setNewUser({ ...newUser, role: e.target.value })}
        >
          {roles.map((role) => (
            <option key={role} value={role}>
              {role}
            </option>
          ))}
        </select>
        <button
          onClick={handleAddUser}
          className="bg-green-600 text-white px-4 py-2 rounded-full hover:bg-green-700 transition"
        >
          Add User
        </button>
      </div>

      <table className="min-w-full bg-surface rounded-lg overflow-hidden text-white">
        <thead>
          <tr className="bg-gray-800 text-gray-300">
            <th className="p-2 border-b border-gray-700 text-left">Name</th>
            <th className="p-2 border-b border-gray-700 text-left">Role</th>
            <th className="p-2 border-b border-gray-700 text-center">Active</th>
            <th className="p-2 border-b border-gray-700 text-left">Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map((u) => (
            <tr key={u.id} className="hover:bg-gray-700 transition-colors">
              <td className="p-2 border-b border-gray-700">{u.name}</td>
              <td className="p-2 border-b border-gray-700">{u.role}</td>
              <td className="p-2 border-b border-gray-700 text-center">
                {u.active ? 'Yes' : 'No'}
              </td>
              <td className="p-2 border-b border-gray-700">
                <button
                  className={`px-3 py-1 rounded-full text-white transition ${
                    u.active ? 'bg-red-600 hover:bg-red-700' : 'bg-green-600 hover:bg-green-700'
                  }`}
                  onClick={() => toggleActive(u.id)}
                >
                  {u.active ? 'Deactivate' : 'Activate'}
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Users;
