
let prescriptions = [];

// Function to fetch prescriptions
async function fetchPrescriptions() {
    try {
        // Retrieve JWT token from local storage
        const token = localStorage.getItem('token');

        // Check if token exists
        if (!token) {
            throw new Error('JWT token not found in local storage');
        }

        // Send GET request with token in headers
        const response = await axios.get('http://localhost:8080/prescriptions', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        // Process the response data
        const prescriptions = response.data;
        // Call a function to render the prescriptions data on the page
        renderPrescriptions(prescriptions);
    } catch (error) {
        console.error('Error fetching prescriptions:', error.message);
        // Display an error message to the user
        alert('Error fetching prescriptions. Please try again later.');
    }
}

// Function to render prescriptions data on the page
function renderPrescriptions(prescriptions) {
    const tableBody = document.getElementById('employees');
    // Clear existing table data
    tableBody.innerHTML = '';

    // Loop through each prescription and create table rows
    prescriptions.forEach(prescription => {
        prescription.medications.forEach(medication => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${prescription.doctorName}</td>
                <td>${prescription.patientName}</td>
                <td>${medication.medicationName}</td>
                <td>${medication.dosage}</td>
                <td>${medication.frequency}</td>
                <td>${medication.instructions}</td>
                <td>${medication.noOfDays}</td>
                <td>
                    <button onclick="editMedication(prescriptions, ${prescription.prescriptionId}, ${medication.medicationId})">Edit</button>
                    <button onclick="deleteMedication(${prescription.prescriptionId}, ${medication.medicationId})">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    });
}

// Function to populate form fields with medication details for editing
function populateFormForEdit(medication) {
    document.getElementById('medicationName').value = medication.medicationName;
    document.getElementById('dosage').value = medication.dosage;
    document.getElementById('frequency').value = medication.frequency;
    document.getElementById('instructions').value = medication.instructions;
    document.getElementById('noOfDays').value = medication.noOfDays;

    // Change the submit button text to "Update"
    document.getElementById('submitButton').textContent = 'Update Medication';
}

// Function to handle editing a medication
function editMedication(prescriptions, prescriptionId, medicationId) {
    // Find the medication in the prescriptions data
    const prescription = prescriptions.find(presc => presc.prescriptionId === prescriptionId);
    
    if (!prescription) {
        console.error(`Prescription not found: ${prescriptionId}`);
        return; // Exit the function if prescription not found
    }

    const medication = prescription.medications.find(med => med.medicationId === medicationId);

    if (!medication) {
        console.error(`Medication not found in prescription: ${medicationId}`);
        return; // Exit the function if medication not found
    }

    // Populate the form fields with the medication details
    populateFormForEdit(medication);
    // Set prescriptionId and medicationId in hidden input fields
    document.getElementById('prescriptionId').value = prescriptionId;
    document.getElementById('medicationId').value = medicationId;
}

// Function to handle form submission and add/update medication to prescription
document.getElementById('medicationForm').addEventListener('submit', function (event) {
    // Prevent the default form submission behavior
    event.preventDefault();

    // Retrieve medication data from the form
    const medicationName = document.getElementById('medicationName').value;
    const dosage = document.getElementById('dosage').value;
    const frequency = document.getElementById('frequency').value;
    const instructions = document.getElementById('instructions').value;
    const noOfDays = document.getElementById('noOfDays').value;

    // Check if the button text is "Add" or "Update"
    const buttonText = document.getElementById('submitButton').textContent;
    if (buttonText === 'Add Medication') {
        // Add new medication
        const medicationData = {
            medicationName: medicationName,
            dosage: dosage,
            frequency: frequency,
            instructions: instructions,
            noOfDays: parseInt(noOfDays) // Convert to integer
        };
        addOrUpdateMedicationToPrescription(medicationData);
    } else if (buttonText === 'Update Medication') {
        // Retrieve prescription ID and medication ID
        const prescriptionId = parseInt(document.getElementById('prescriptionId').value);
        const medicationId = parseInt(document.getElementById('medicationId').value);
        const medicationData = {
            medicationName: medicationName,
            dosage: dosage,
            frequency: frequency,
            instructions: instructions,
            noOfDays: parseInt(noOfDays) // Convert to integer
        };
        addOrUpdateMedicationToPrescription(medicationData, prescriptionId, medicationId);
    }
});

// Function to add or update a medication in a prescription
async function addOrUpdateMedicationToPrescription(medicationData, prescriptionId, medicationId) {
    try {
        // Retrieve JWT token from local storage
        const token = localStorage.getItem('token');

        // Check if token exists
        if (!token) {
            throw new Error('JWT token not found in local storage');
        }

        if (prescriptionId && medicationId) {
            // Update existing medication
            const response = await axios.put(`http://localhost:8080/prescriptions/${prescriptionId}/medications/${medicationId}`, medicationData, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            // Log the response or handle it as needed
            console.log('Medication updated successfully:', response.data);
        } else {
            // Add new medication
            const response = await axios.post('http://localhost:8080/prescriptions/1/medications', medicationData, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            // Log the response or handle it as needed
            console.log('Medication added successfully:', response.data);
        }

        // Reset the form to its initial state
        resetForm();
        // Refresh prescriptions data after adding or updating medication
        fetchPrescriptions();
    } catch (error) {
        console.error('Error adding/updating medication:', error.message);
        // Display an error message to the user
        alert('Error adding/updating medication. Please try again later.');
    }
}

// Function to delete medication
async function deleteMedication(prescriptionId, medicationId) {
    try {
        // Retrieve JWT token from local storage
        const token = localStorage.getItem('token');

        // Check if token exists
        if (!token) {
            throw new Error('JWT token not found in local storage');
        }

        // Send DELETE request with token in headers
        const response = await axios.delete(`http://localhost:8080/prescriptions/${prescriptionId}/medications/${medicationId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        // Log success message
        console.log('Medication deleted successfully:', response.data);
        // Refresh prescriptions data after deleting medication
        fetchPrescriptions();
    } catch (error) {
        console.error('Error deleting medication:', error.message);
        // Display an error message to the user
        alert('Error deleting medication. Please try again later.');
    }
}

// Call the function to fetch prescriptions when the page loads
fetchPrescriptions();

// Function to reset the form to its initial state
function resetForm() {
    // Clear the form fields
    document.getElementById('medicationForm').reset();
    // Set the button text back to "Add"
    document.getElementById('submitButton').textContent = 'Add Medication';
    // Clear prescriptionId and medicationId hidden fields
    document.getElementById('prescriptionId').value = '';
    document.getElementById('medicationId').value = '';
}
