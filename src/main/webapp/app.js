document.getElementById("btnFetchData").addEventListener("click", () => {
    fetch("/api/hello")
        .then(response => {
            if (!response.ok) {
                throw new Error("Error en la solicitud: " + response.status);
            }
            return response.json();
        })
        .then(data => {
            document.getElementById("output").textContent = data.message;
        })
        .catch(error => {
            console.error(error);
            document.getElementById("output").textContent = "Error: " + error.message;
        });
});
console.log("¡Script cargado correctamente!");
