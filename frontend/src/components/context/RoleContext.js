import React, { createContext, useState } from 'react';

export const RoleContext = createContext();

export const RoleProvider = ({ children }) => {
    const [role, setRole] = useState(null);

    const selectRole = (selectedRole) => {
        setRole(selectedRole);
    };

    const clearRole = () => {
        setRole(null);
    };

    return (
        <RoleContext.Provider value={{ role, selectRole, clearRole }}>
            {children}
        </RoleContext.Provider>
    );
};