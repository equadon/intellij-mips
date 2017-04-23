# this is a comment
.data
myString: .asciiz "Hello, world!\n"

.text
main:
  li $v0, 4
  la $a0, myString
  syscall

  li $v0, 10
  syscall

