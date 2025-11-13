.EXPORT_ALL_VARIABLES:

APP_NAME=pacman-bot
IMAGE_REGISTRY=gtf-system-registry.ap-southeast-5.cr.aliyuncs.com/gopay-systems
IMAGE_NAME=$(IMAGE_REGISTRY)/$(APP_NAME)
IMAGE_TAG=$(CI_COMMIT_SHORT_SHA)
GIT_TAG=$(shell git describe --exact-match --tags HEAD 2>/dev/null)

# This command should be used to create the jar file
# generated jar will be used within the Dockerfile
.PHONY: build.binaries
build.binaries:
	./gradlew build -x check
