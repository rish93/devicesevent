# devicesevent
DevicesEvent Multitenant Application  designed to consume events triggered by a Kafka topic when a file is added to an S3 bucket.


Java Backend- Java+ AWS

Create multi-tenant Spring Boot application designed to consume events triggered 
by a Kafka topic when a file is added to an S3 bucket.
The application should retrieve the file from tenant specific S3 bucket
Execute necessary processing on the file content, and persist the data into tenant specific database.
Expose REST APIs to facilitate the retrieval of ingested data where input of API should be tenant_id and device_id
Ensure that the S3 file is deleted after successful processing, and implement robust handling for failure scenarios,
such as errors during S3 file processing or issues during database ingestion.
Publish alerts in the event of any failures.(Optional)

Sample file for reference -

Tenant_id    Device_id  Model              Manufacturer       Device_Type        Approval_Date
Manipal_01   201        GE-MRI-1000        General Electric   MRI Scanner        2022-01-20
Manipal_01   202        GE-Xray-5000       General Electric   X-ray Machine      2022-03-15
Manipal_01   203        GE-Ultrasound-300  Siemens            Ultrasound System  2022-02-15
Manipal_01   204        GE-CT-Scanner-700  Phillips           CT Scanner         2022-02-10



![alt text](https://github.com/rish93/devicesevent/blob/master/architecture.jpg?raw=true)
