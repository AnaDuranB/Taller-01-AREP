document.getElementById("btnSendData").addEventListener("click", async () => {
    const inputData = document.getElementById("inputData").value;

    if (!inputData) {
        alert("Por favor, escribe algo antes de enviar.");
        return;
    }

    // Hacer una solicitud al backend
    try {
        const response = await fetch("/api/hello");
        if (response.ok) {
            const data = await response.json();
            document.getElementById("output").innerText =
                `Respuesta: ${data.message} - Recibido: ${inputData}`;
        } else {
            document.getElementById("output").innerText = "Error: API no encontrada.";
        }
    } catch (error) {
        document.getElementById("output").innerText = "Error al conectar con el servidor.";
    }
});
