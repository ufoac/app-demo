#!/bin/bash

DEFAULT_IMAGE_NAME="app-demo"
DEFAULT_TAG="v1"

IMAGE_NAME=${DEFAULT_IMAGE_NAME}
TAG=${1:-$DEFAULT_TAG}

FULL_IMAGE_NAME="${IMAGE_NAME}:${TAG}"

echo "开始制作镜像..."
DOCKER_BUILDKIT=1 docker build -t "${FULL_IMAGE_NAME}" -f ./Dockerfile ../../../
if [ $? -ne 0 ]; then
    echo "镜像制作失败，请检查错误信息。"
    exit 1
fi
echo "镜像成功打包为 ${FULL_IMAGE_NAME}。"

