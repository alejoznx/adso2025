import React, { useState, useEffect}from "react";
import { View, Text, FlatList, TouchableOpacity, Alert,TextInput, Modal, ActivityIndicator, ScrollView } from "react-native";
import {categoriesStyles} from "../styles/CategoriesStyles";
import {categoryService, authService} from "../services/api";

export default function CategoriesScreen() {
const [categories, setCategories] = useState<any[]>([]);
const [loading, setLoading] = useState(false);
const [modalVisible, setModalVisible] = useState(false);
const [editing, setEditing] = useState<any>(null);
const [formData, setFormData] = useState({name: "", description: ""});
const [error, setError] = useState("");
const [currentUser, setCurrentUser] = useState<any>(null);

useEffect(() => {
    loadCurrentUser();
    loadCategories();
}, []);
const loadCurrentUser = async () => {
        try {
        const user = await authService.getCurrentUser();
        setCurrentUser(user);
        } catch (error) {
        console.error("Error al cargar el usuario:", error);
        }
    };
    const loadCategories = async () => {
        setLoading(false);
         setError("");
        try {
        const response = await categoryService.getAll();
        setCategories(response?.data || []);
        } catch (error) {
          setError("Error al cargar las categorías.");
        } finally {
          setLoading(false);  
        }
    }

const handleSave = async () => {
    if (!formData.name.trim()) {
        Alert.alert("Error de Validación", "El nombre es obligatorio.");
        return;
    }
    try {
        if (editing) {
        await categoryService.update(editing.id, formData);
        Alert.alert("Éxito", "Categoría actualizada correctamente.");
        }else {
        await categoryService.create(formData);
        Alert.alert("Éxito", "Categoría creada correctamente.");
        }
        setModalVisible(false);
        resetForm();
        loadCategories();
    } catch (error) {
        Alert.alert("Error", "Error al guardar la categoría.");
    }
}
const  handDelete = (item: any) => {
    if  (currentUser?.role !==  "ADMIN") {
        Alert.alert("Permiso Denegado", "No tienes permiso para eliminar categorías.");
        return;
    }
    Alert.alert("Confirmar",`¿Eliminar ${item.name}?`, [
        {text: "Cancelar", style: "cancel"},
        {
            text: "Eliminar", 
            style: "destructive", 
            onPress: async () => {
            try {
                await categoryService.delete(item.id);
                Alert.alert("Exito,", "Categoria Eliminada")
                loadCategories();

            }catch(error){
                Alert.alert("Error","No se puede eliminar");
            }
        }
    }

    ]);

};

    const handleToggleActive = (item:any) => {
        const action = item.active ? "Desactivar" :
        "Activar";
        Alert.alert("Confirmar", `¿${action.charAt(0).toUpperCase() + action.slice(1)} ${item.name}?` ,[
            {text: "Cancelar", style: "cancel"},
            {
                text: action.charAt(0).toUpperCase() + action.slice(1),onPress: async () => {
                    try {
                        await categoryService.update(item.id,{
                            name: item.name,
                            description: item.description,
                            active:item.active
                        });
                        Alert.alert("Exito", `Categoria ${item.active ? 'desactivada' : 'activada'}`);
                        loadCategories();
                    } catch (error) {
                        Alert.alert("Error", `No se pudo ${action.toLowerCase()}`);
                    }
                }
            }
        ]);
    };

    const handleEdit = (item: any) => {
        setFormData({name: item.name, description: item.description || ""});
        setEditing(item);
        setModalVisible(true);
};

    const resetForm = () => {
    setFormData({name: "", description: ""});
    setEditing(null);
    };

    const renderCategory = ({item}: {item: any}) => (
    <View style={categoriesStyles.categoryCard}>
        <View style={categoriesStyles.categoryInfo}>
            <Text style={categoriesStyles.categoryName}>
                {item.name} {!item.active && <Text style={{color: "#999"}}>(Inactiva) 
                </Text>}
            </Text>
            {item.description && (
                <Text style={categoriesStyles.categoryDescription}>{item.description}</Text>
            )} 
            </View>
            <View style={categoriesStyles.actionsContainer}>
                <TouchableOpacity
                 style={[categoriesStyles.actionButton, categoriesStyles.editButton]} 
                onPress={() => handleEdit(item)}
                >

                    <Text style={[categoriesStyles.actionButtonText, categoriesStyles. editButtonText]}>Editar</Text>
                </TouchableOpacity>
                 <TouchableOpacity
                 style={[categoriesStyles.actionButton, item.active ? categoriesStyles.deleteButton : categoriesStyles.editButton]} 
                onPress={() => handleToggleActive(item)}
                >

                <Text style={[categoriesStyles.actionButtonText, item.active ? categoriesStyles.deleteButtonText : categoriesStyles.editButtonText]}>
                    {item.active ? "Desactivar" : "Activar"}
                </Text>
                </TouchableOpacity>
               {currentUser?.role === "ADMIN" && (
                <TouchableOpacity
                    style={[categoriesStyles.actionButton, categoriesStyles.deleteButton]}
                    onPress={() => handDelete (item)}
                >
                    <Text style={[categoriesStyles.actionButtonText, categoriesStyles.deleteButtonText]}>Eliminar</Text>
                    </TouchableOpacity>
                 )} 
            </View>
        </View>
    );

    if (loading){
        return (
            <view style={categoriesStyles.loadingContainer}>
                <ActivityIndicator size="large" color="#007Aff"/>
                <text style={categoriesStyles.loadingText}>Cargando categorías...</text>
            </view>
        );
    }

    return(
    <View style={categoriesStyles.container}>
        <View style={categoriesStyles.header}>
            <View style={categoriesStyles.headerContent}>
                <Text style={categoriesStyles.HeaderTitle}>Gestion de Categorías</Text>
                <TouchableOpacity
                    style={categoriesStyles.addButton}
                    onPress={() => {
                        resetForm();
                        setModalVisible(true);
                    }}
                >
                    <Text style={categoriesStyles.addButtonText}>+ Nueva </Text>
                </TouchableOpacity>
            </View>
        </View>

        {error ? (
            <View style={categoriesStyles.errorContainer}>
                <Text style={categoriesStyles.errorText}>{error}</Text>
                <TouchableOpacity style={categoriesStyles.retryButton} onPress={loadCategories}>
                    <Text style={categoriesStyles.retryButtonText}>Reintentar</Text>
                </TouchableOpacity>
            </View>
        ) : null}

        <FlatList
            data={categories}
            renderItem={renderCategory}
            keyExtractor={(item) => item.id.toString() || ``}
            contentContainerStyle={categoriesStyles.listContent}
            showsVerticalScrollIndicator={false}
            ListEmptyComponent={
                loading && !error ? (
                    <View style={categoriesStyles.ListEmptyContainer}>   
                        <Text style={categoriesStyles.ListEmptyText}>No hay categorías.</Text>
                        <Text style={categoriesStyles.ListEmptySubtext}>Toca "Nueva" para comenzar.</Text>
                    </View>
                ) : null
            }
        />  

         <Modal animationType="slide" transparent={true} visible={modalVisible}>
            <View style={categoriesStyles.modalOverlay}>
                    <View style={categoriesStyles.modalContent}>
                        <ScrollView>
                            <View style={categoriesStyles.modalHeader}>
                                <Text style={categoriesStyles.modalTitle}>
                                    {editing ? "Editar Categoría" : "Nueva Categoría"}
                                </Text>
                            </View>

                                <View style={categoriesStyles.formContainer}>
                                <View style={categoriesStyles.inputGroup}>
                                    <Text style={categoriesStyles.inputLabel}>Nombre *</Text>
                                    <TextInput
                                        style={categoriesStyles.input}
                                        value={formData.name}
                                        onChangeText={(text) => setFormData({...formData, name: text})}
                                        placeholder="Nombre de la categoría"
                                        placeholderTextColor="#999"
                                    />
                                </View>

                                 <View style={categoriesStyles.inputGroup}>
                                        <Text style={categoriesStyles.inputLabel}>Descripcion</Text>
                                        <TextInput
                                            style={[categoriesStyles.input, categoriesStyles.textArea]}
                                            value={formData.description}
                                            onChangeText={(text) => setFormData({...formData, description: text})}
                                            placeholder="Descripción de la categoría"
                                            placeholderTextColor="#999"
                                            multiline
                                            numberOfLines={3}
                                            textAlignVertical="top"
                                        />
                                    </View>
                                </View>

                                    <View style={categoriesStyles.modalButtons}>
                                        <TouchableOpacity
                                            style={[categoriesStyles.modalButton, categoriesStyles.cancelButton]}
                                            onPress={() => {setModalVisible(false);}}
                                        >
                                            <Text style={[categoriesStyles.modalButtonText, categoriesStyles.cancelButtonText]}>Cancelar</Text>
                                        </TouchableOpacity>
                                        <TouchableOpacity
                                            style={[categoriesStyles.modalButton, categoriesStyles.saveButton]}
                                            onPress={handleSave}
                                        >
                                            <Text style={[categoriesStyles.modalButtonText, categoriesStyles.saveButtonText]}>
                                            {editing ? "Actualizar" : "Crear"}
                                        </Text>
                                    </TouchableOpacity>
                            </View>
                        </ScrollView>
                    </View>
                </View>
            </Modal>
        </View>
    );
}