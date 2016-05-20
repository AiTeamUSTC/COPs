for dim in 10 30; do
eval=$(($dim * 20000))
pop=$(($dim * 100))
echo
echo "$dim D problems"
echo
time for p in 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18; do
	echo "C$p: "
	./eDEa -len $dim -delta 0.0001 -eval $eval -p $p -pop $pop -e -er 0.2 -gen 5000 -g -gr 0.1 -gs 3 -F 0.5 -e0r 0.9 -report -1 $*
	echo
done
done
