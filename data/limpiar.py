import pandas as pd

# Cargar el CSV original
df = pd.read_csv(r'C:\Users\sandr\Desktop\MecanicSync\data\all-vehicles-model.csv', sep=';')

# Seleccionar solo las columnas relevantes
df_clean = df[['Make', 'Model', 'Fuel Type1', 'Year']].copy()

# Renombrar columnas para que coincidan con tu esquema SQL
df_clean = df_clean.rename(columns={
    'Make': 'marca',
    'Model': 'modelo',
    'Fuel Type1': 'tipo_motor',
    'Year': 'anio_inicio'
})

# Eliminar duplicados (importante para no repetir marcas o modelos)
df_clean = df_clean.drop_duplicates()

# Guardar el resultado limpio
df_clean.to_csv('vehiculos_modelos_limpio.csv', index=False)

print("✅ Archivo limpio guardado como vehiculos_modelos_limpio.csv")