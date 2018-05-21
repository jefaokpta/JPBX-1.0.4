#!/bin/bash

array=(`mysql -ujpbx -pjpbxadmin jpbx -e "select channel from conference_aux where room=$1"`)
mysql -ujpbx -pjpbxadmin jpbx -e "delete conference_aux where room=$1"

for ((i=1;i<${#array[*]};i++)) do
	asterisk -rx "hangup request ${array[$i]}"
	sleep 1
done