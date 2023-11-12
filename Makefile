MONGODB_CONTAINER=mongodb_spring6course
SHELL=sh

.DEFAULT_GOAL=up

# Lift up the dev. environment
up:
		docker compose -f ./docker/docker-compose.yml up --build

# Take down the dev. environment
down:
		docker compose -f ./docker/docker-compose.yml down

# Take down the dev. environment and remove containers, images
fdown:
		docker compose down --rmi all

# Connect to the shell of the container of the nodejs application
shellmysql:
		docker exec -it ${MONGODB_CONTAINER} ${SHELL}
