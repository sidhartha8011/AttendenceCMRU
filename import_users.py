import csv,firebase_admin
from firebase_admin import auth, credentials
cred = credentials.Certificate("/Users/sidharthaparasramka/AndroidStudioProjects/AttendenceCMRU/script_id.json")
firebase_admin.initialize_app(cred)

# Read the CSV file
with open("users.csv", "r") as file:
    reader = csv.reader(file)
    # Skip the first row (header)
    next(reader)
    for row in reader:
        email = row[0]
        password = row[1]
        # Create the user account
        user = auth.create_user(email=email, password=password)
        print("User created with email:", email)
