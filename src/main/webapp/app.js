document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("componentForm");
    const componentList = document.getElementById("componentList");
    const componentsHeader = document.getElementById("componentsHeader");
    const componentTable = document.getElementById("componentTable");

    fetchComponents();

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const name = document.getElementById("name").value;
        const type = document.getElementById("type").value;
        const price = document.getElementById("price").value;

        if (!name || !type || !price) {
            alert("Todos los campos son obligatorios.");
            return;
        }

        const component = { name, type, price: parseFloat(price) };

        try {
            await fetch("http://localhost:35000/api/components", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(component),
            });

            form.reset();
            fetchComponents();
        } catch (error) {
            console.error("Error al agregar el componente:", error);
        }
    });

    async function fetchComponents() {
        try {
            const response = await fetch("http://localhost:35000/api/components");
            const components = await response.json();

            componentList.innerHTML = '';

            if (components.length > 0) {
                componentsHeader.style.display = "block";
                componentTable.style.display = "block";

                components.forEach(({ name, type, price }) => {
                    const row = document.createElement("tr");
                    row.innerHTML = `
                        <td>${name}</td>
                        <td>${type}</td>
                        <td>$${price.toFixed(2)}</td>
                    `;
                    componentList.appendChild(row);
                });
            } else {
                componentsHeader.style.display = "none";
                componentTable.style.display = "none";
            }
        } catch (error) {
            console.error("Error al obtener componentes:", error);
        }
    }
});
