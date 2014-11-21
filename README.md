Sirat is using annotation like in hibernate (http://hibernate.org/) to set some attribute in java. Actually this is what sirat do can convert from this :

When we called 

		Sirat.generateQuery(Dog.class))
		
It will convert this class :

		class Dog{
			@PRIMARY_KEY private String name;
			@NOT_NULL private String type;
			private long birthDate;
			@UNIQUE private int uuid;
			@Ignore private String birthPlace;
		}
		
Into this class :
		
		CREATE TABLE 'Dog' (
			'type' TEXT NOT NULL , 
			'name' TEXT, 
			'uuid' INTEGER, 
			'birthDate' LONG,  
			PRIMARY KEY ('name'),  
			UNIQUE ('uuid')
		)