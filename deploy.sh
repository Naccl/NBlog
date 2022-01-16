#!/bin/bash
# Author: Naccl
#
# This script is used to deploy NBlog to my server, apply my changes
# on the "prod" branch for myself.
# I didn't upload "prod" branch because it has my own configuration,
# so you may not be able to use it.

project_local_path=~/work/idea-project/NBlog
dev_branch=master
prod_branch=prod
server=aliyun
api_path=/home/nblog/api
cms_path=/home/nblog/cms
view_path=/home/nblog/view


function echo_msg()
{
  case $1 in
    0)
      echo -e "\033[30;42m $2 \033[0m"
      ;;
    1)
      echo -e "\033[30;41m $2 \033[0m"
      ;;
    *)
      echo "$2"
      ;;
  esac
}

module_name=''
server_path=''

function menu()
{
  echo -e "\033[35m(1)blog-api\033[0m"
  echo -e "\033[35m(2)blog-cms\033[0m"
  echo -e "\033[35m(3)blog-view\033[0m"
  
  read -p "choice a module to deploy: " module
  case $module in
    1)
      echo_msg 0 "deploy blog-api"
      module_name=blog-api
      server_path=$api_path
      deploy_springboot
      ;;
    2)
      echo_msg 0 "deploy blog-cms"
      module_name=blog-cms
      server_path=$cms_path
      deploy_vue
      ;;
    3)
      echo_msg 0 "deploy blog-view"
      module_name=blog-view
      server_path=$view_path
      deploy_vue
      ;;
    *)
      echo_msg 1 "ERROR INPUT"
      exit 1
  esac
  echo_msg 0 "DONE"
}

function deploy_springboot()
{
  git_merge
  echo_msg 0 "build $module_name"
  mvn clean package -Dmaven.test.skip=true
  echo_msg 0 "scp target jar to server"
  scp target/blog-api-0.0.1.jar ${server}:${server_path}
  read -p "restart $module_name right now? [y/n]: " restart
  case $restart in
    y)
      ssh $server "cd ${server_path} && sh run.sh"
      ;;
    *)
      ;;
  esac
}

function deploy_vue()
{
  git_merge
  echo_msg 0 "build $module_name"
  npm run build
  echo_msg 0 "create dir to server if not exist, clean server dist dir if exist"
  ssh $server "rm -rf $server_path/dist && mkdir -p $server_path/dist"
  echo_msg 0 "scp dist to server"
  scp -r dist ${server}:${server_path}
}

function git_merge()
{
  cd ${project_local_path}/${module_name}
  branch=$(git rev-parse --abbrev-ref HEAD)
  if [[ $branch = $dev_branch ]]; then
    echo_msg 0 "branch in $branch, checkout to $prod_branch"
    git checkout $prod_branch
    echo_msg 0 "merge $branch into $prod_branch"
    git merge -m "Merge branch '$branch' into $prod_branch" "$branch"
  elif [[ $branch = $prod_branch ]]; then
    echo_msg 0 "branch already in $prod_branch"
  else
    echo_msg 1 "ERROR branch in '$branch'"
    exit 1
  fi
}

menu
read -n 1 -p "Press any key to exit..."