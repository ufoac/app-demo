#!/bin/bash

DEFAULT_REPO_PREFIX="172.0.0.1:5000/cao"  # 默认仓库前缀
if [ "$#" -lt 2 ]; then
    echo "用法: $0 <image_name> <tag> [repo_prefix]"
    echo "仓库前缀可选，默认是${DEFAULT_REPO_PREFIX}。"
    exit 1
fi

IMAGE_NAME=$1
TAG=$2
REPO_PREFIX=${3:-$DEFAULT_REPO_PREFIX}
FULL_IMAGE_NAME="${REPO_PREFIX}/${IMAGE_NAME}:${TAG}"

echo "目标镜像: ${FULL_IMAGE_NAME}"
echo "开始标记镜像..."
docker tag "${IMAGE_NAME}:${TAG}" "${FULL_IMAGE_NAME}"
if [ $? -ne 0 ]; then
    echo "标记失败，请检查错误信息。"
    exit 1
fi
echo "镜像成功标记为 ${FULL_IMAGE_NAME}"

echo "开始推送镜像..."
docker push "${FULL_IMAGE_NAME}"
if [ $? -ne 0 ]; then
    echo "推送失败，请检查错误信息。"
    exit 1
fi
echo "镜像 ${FULL_IMAGE_NAME} 成功推送到远端仓库。"


