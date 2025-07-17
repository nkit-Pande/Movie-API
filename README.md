# 🎬 Movie API – Spring Boot on AWS EKS

A production-ready RESTful API for movie management, showcasing modern backend development with Spring Boot and cloud-native deployment on AWS EKS.

## 🛠️ Tech Stack

### Core Technologies
- **Java 17** - Modern Java features and performance
- **Spring Boot 3.5.3** - Rapid application development
- **Spring Security** - Authentication & Authorization
- **JWT** - Secure token-based authentication
- **Spring Data JPA/Hibernate** - ORM and database operations
- **MySQL 8.0+ (RDS)** - Relational database
- **AWS S3** - File storage and retrieval
- **Docker** - Containerization
- **Kubernetes (EKS)** - Container orchestration
- **Maven** - Dependency management

## 🎯 Key Learnings

### Backend Development
- Implemented RESTful API following industry best practices
- Designed and developed secure authentication system using JWT
- Created role-based access control (USER/ADMIN)
- Implemented file upload functionality with AWS S3 integration
- Developed efficient database schema and queries
- Integrated pagination and sorting for optimal data retrieval and performance

### DevOps & Cloud
- Containerized application using Docker
- Set up and configured Amazon EKS cluster
- Deployed and managed MySQL on Amazon RDS
- Configured IAM roles and security groups
- Deployed and managed AWS S3 bucket

### Testing
- Unit testing with JUnit 5
- API testing with MockMVC
- Test coverage and quality assurance

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Docker 20.10+
- AWS CLI configured
- kubectl

### Local Development
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/movie-api.git
   cd movie-api
   ```

2. Configure `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/moviedb
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   aws.s3.bucket-name=your-s3-bucket
   aws.region=your-region
   jwt.secret=your-jwt-secret
   jwt.expiration=86400000
   ```

3. Build and run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

## 🐳 Deployment

### Build and Push to ECR
```bash
docker build -t movie-api:latest .
aws ecr get-login-password --region <your-region> | docker login --username AWS --password-stdin <your-account-id>.dkr.ecr.<your-region>.amazonaws.com
docker tag movie-api:latest <your-account-id>.dkr.ecr.<your-region>.amazonaws.com/movie-api:latest
docker push <your-account-id>.dkr.ecr.<your-region>.amazonaws.com/movie-api:latest
```

### Deploy to EKS
1. Update `k8s.yaml` with your RDS endpoint and credentials
2. Apply the Kubernetes manifests:
   ```bash
   kubectl apply -f k8s.yaml
   ```
3. Verify deployment:
   ```bash
   kubectl get pods
   kubectl get services
   ```

