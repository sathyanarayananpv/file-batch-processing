# large-file-batch-processing

## Installation :
	1. Clone the source from GitHub
	2. Import the project as "Existing Maven Project" in eclipse IDE
	3. Do Maven Update
	4. Run the class file BatchFileProcessorInterviewApplication.java


## Configurations :
	1. File Resources(application.properties)
	   i) Input - Project folder/input 
	   ii) output - Project folder/output
	2. Chunk Size : Chunksize is set to 1000 in BatchFileConstants.java::CHUNK_SIZE, shall be used for scaling further.
	3. Common text and CSV files can be easily customized by configuring below 3 steps 
		i) Create new bean for CustomItemWriter(pass delimiter & filename) 
		ii) Add unique file type in the enum FileTypes in BatchFileConstants.java
		iii) Inject and add the new bean in FileWriterFactory.java


## Testing :
	1. Rest controller is provided to schedule the job,JOB ID will be sent as response once job scheduled. Websocket can be added to monitor the realtime statistics of the upload.
	2. To test the application hit : http://localhost:8080/launchJob,Output file names will contains JobID as prefix
	
## Design pattern details
	Factory pattern is used to for injecting objects for CompositeItemWriters. 
	Check the method BatchFileProcessorConfig.compositeWriter() and the class FileWriterFactory.java