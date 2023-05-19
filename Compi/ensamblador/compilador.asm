;DIRECTIVAS de ensmablador por acuerdo se escribiran en MAYUSCULAS y en la primera columnaumnaa de la izquierda
.MODEL SMALL
.586
.STACK  100h
.DATA
	result db 0
	Provisional db 0
	firstDigit db 0
	secondDigit db 0
	a db 0
	b db 0
	c db 0
	d db 0
	e db 0
	f db 0
.code
.STARTUP ;DIRECTIVA para el inicio de ensamblador
	mov CL, a
	add CL, 48
	mov Provisional, CL
	mov AH, 2h
	mov DL, Provisional
	int 21h

	mov ah, 02h
	mov dl, 0ah ;salto de lineaa
	int 21h
	mov ah, 02h
	mov dl, 0dh ;retorno de carro
	int 21h

	mov ah, 0ah
	lea dx, d
	int 21h
	mov al, firstDigit
	mov bl, secondDigit
	mul bl
	mov result, al

	mov ah, 09h
	lea dx, result
	int 21h

	mov al, result
	aam
	mov bx, ax
	mov ah, 02h
	mov dl, bh
	add dl, 30h
	int 21h

	mov ah, 02h
	mov dl, dl
	add dl, 30h
	int 21h
	mov al, firstDigit
	sub al, secondDigit
	mov result, al

	mov ah, 09h
	lea dx, result
	int 21h

	mov al, result
	aam
	mov bx, ax
	mov ah, 02h
	mov dl, bh
	add dl, 30h
	int 21h

	mov ah, 02h
	mov dl, dl
	add dl, 30h
	int 21h
	mov al, firstDigit
	add al, secondDigit
	mov result, al

	mov ah, 09h
	lea dx, result
	int 21h

	mov al, result
	aam
	mov bx, ax
	mov ah, 02h
	mov dl, bh
	add dl, 30h
	int 21h

	mov ah, 02h
	mov dl, dl
	add dl, 30h
	int 21h
.EXIT ;DIRECTIVA donde terminará el ensamblador
END