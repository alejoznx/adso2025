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
    fetchCurrentUser();
    fetchCategories();
}, []);
const loadCurrentUser = async () => {
        try {
        const user = await authService.getCurrentUser();
        setCurrentUser(user);
        } catch (error) {
        console.error("Error loading current user:", error);
        }
    };
    const loadCategories = async () => {
        setLoading(false);
         setError("");
        try {
        const response = await categoryService.getAll();
        setCategories(response?.data || []);
        } catch (error) {
          setError("Failed to load categories.");
        } finally {
          setLoading(false);  
        }
    }

const handleSave = async () => {
    if (!formData.name.trim()) {
        Alert.alert("Validation Error", "Name is required.");
        return;
    }
    try {
        if (editing) {
        await categoryService.update(editing.id, formData);
        Alert.alert("Success", "Category updated successfully.");
        }else {
        await categoryService.create(formData);
        Alert.alert("Success", "Category created successfully.");
        }
        setModalVisible(false);
        resetForm();
        loadCategories();
    } catch (error) {
        Alert.alert("Error", "Failed to save category.");
    }
}
const  handDelete = (item: any) => {
    if  (currentUser?.role !==  "ADMIN") {
        Alert.alert("Permission Denied", "You do not have permission to delete categories.");
        return;
    }
    Alert.alert("Confirm",`¿eliminar ${item.name}?`, [
        {text: "Cancel", style: "cancel"},
        {text: "Delete", style: "destructive", onPress: async () => {
            try {
                

            }
        }
    }
    ]);

}
}